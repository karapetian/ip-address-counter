package com.lightspeed.tasks.counter.splitter;

import com.lightspeed.tasks.counter.exception.FileSplittingException;
import com.lightspeed.tasks.counter.exception.SourceFileNotFoundException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSplitter {

    private static final char NEW_LINE = '\n';

    private long chunkSizeInBytes = 5_000_000;

    private final Path sourceFile;

    public FileSplitter(String sourceFileLocation) {
        this.sourceFile = Paths.get(sourceFileLocation);
    }

    public FileSplitter(String sourceFileLocation, long chunkSize) {
        this(sourceFileLocation);
        this.chunkSizeInBytes = chunkSize;
    }

    /**
     * @param fileChunkRange representing one file chunk
     * @return the lines from the given chunk
     */
    public List<String> getLinesPerChunk(FileChunkRange fileChunkRange) {
        try (FileChannel channel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
            channel.position(fileChunkRange.startOffset());
            ByteBuffer buffer = ByteBuffer.allocate(
                    (int) (fileChunkRange.endOffset() - fileChunkRange.startOffset() + 1));

            channel.read(buffer);

            String chunk = new String(buffer.array(), StandardCharsets.UTF_8).trim();
            return Arrays.stream(chunk.split(String.valueOf(NEW_LINE)))
                    .filter(line -> !line.trim().isEmpty())
                    .toList();
        } catch (IOException e) {
            throw new FileSplittingException("Failed to read from " + fileChunkRange + " ranges.", e);
        }
    }

    /**
     * Splits the big source file into chunks.
     *
     * @return a list of FileChunkRanges representing file chunks
     */
    public List<FileChunkRange> extractFileChunks() {
        if (!Files.exists(sourceFile)) {
            throw new SourceFileNotFoundException("File cannot be found: " + sourceFile);
        }

        long startingPos = 0;
        List<FileChunkRange> chunkRanges = new ArrayList<>();
        while (true) {
            long endingPos = calculateEndPosition(startingPos, chunkSizeInBytes);
            if (endingPos == -1) {
                break;
            }
            chunkRanges.add(new FileChunkRange(startingPos, endingPos));
            startingPos = endingPos + 1;
        }
        return chunkRanges;
    }

    private long calculateEndPosition(long startingPos, long chunkSize) {
        try (FileChannel channel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
            if (startingPos >= channel.size()) {
                return -1;
            }

            long endingPos = startingPos + chunkSize;
            if (endingPos >= channel.size()) {
                endingPos = channel.size() - 1;
            }

            channel.position(endingPos);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readBytes = channel.read(buffer);

            if (readBytes > 0) {
                for (int i = 0; i < readBytes; i++) {
                    if (buffer.get(i) == NEW_LINE) {
                        break;
                    }
                    endingPos++;
                }
            }

            return endingPos;
        } catch (IOException e) {
            throw new FileSplittingException("Failed to calculate the endingPos for the startingPos " + startingPos, e);
        }
    }
}
