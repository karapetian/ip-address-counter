package com.lightspeed.tasks.counter.splitter;

/**
 * Holds stating and ending positions of a file chunk.
 */
public record FileChunkRange(Long startOffset, Long endOffset) {

}
