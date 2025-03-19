package com.lightspeed.tasks.counter.counter;

public class IPConverter {

    private IPConverter() {
    }

    /**
     * Converts an IP address into its int representation.
     * @param ipAddress IP as a string
     * @return an int number representing the ipAddress
     */
    public static int convertIPToInt(String ipAddress) {
        String[] octets = splitIpAddress(ipAddress);
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (Integer.parseInt(octets[i]) << (24 - (8 * i)));
        }
        return result;
    }

    private static String[] splitIpAddress(String ipAddress) {
        String[] parts = new String[4];
        int start = 0;
        int index = 0;
        for (int i = 0; i < ipAddress.length(); i++) {
            if (ipAddress.charAt(i) == '.') {
                parts[index++] = ipAddress.substring(start, i);
                start = i + 1;
            }
        }
        parts[index] = ipAddress.substring(start);
        return parts;
    }
}
