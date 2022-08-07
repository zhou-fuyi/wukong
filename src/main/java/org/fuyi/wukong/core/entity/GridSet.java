package org.fuyi.wukong.core.entity;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 4/8/2022 9:26 pm
 * @since: 1.0
 **/
public class GridSet {

    private static final String DEFAULT_DELIMITER = ",";
    private static final boolean DEFAULT_LONGITUDE_FIRST = false;

    private String longitudeInterval;

    private String latitudeInterval;

    private String delimiter = DEFAULT_DELIMITER;

    private boolean longitudeFirst = DEFAULT_LONGITUDE_FIRST;

    public GridSet() {
    }

    public GridSet(String longitudeInterval, String latitudeInterval) {
        this.longitudeInterval = longitudeInterval;
        this.latitudeInterval = latitudeInterval;
    }

    public String getLongitudeInterval() {
        return longitudeInterval;
    }

    public void setLongitudeInterval(String longitudeInterval) {
        this.longitudeInterval = longitudeInterval;
    }

    public String getLatitudeInterval() {
        return latitudeInterval;
    }

    public void setLatitudeInterval(String latitudeInterval) {
        this.latitudeInterval = latitudeInterval;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isLongitudeFirst() {
        return longitudeFirst;
    }

    public void setLongitudeFirst(boolean longitudeFirst) {
        this.longitudeFirst = longitudeFirst;
    }

    /**
     * 格网实例集构建
     *
     * @return
     */
    public List<String> instances() {
        if (!StringUtils.hasText(longitudeInterval) || !StringUtils.hasText(latitudeInterval)) {
            return Collections.EMPTY_LIST;
        }
        Object[] longInterval = Arrays.stream(longitudeInterval.split(delimiter)).filter(StringUtils::hasText).toArray();
        if (longInterval.length != 2) {
            throw new IllegalArgumentException(String.format("The wrong longitude interval parameter, %s", Arrays.toString(longInterval)));
        }
        Object[] latInterval = Arrays.stream(latitudeInterval.split(delimiter)).filter(StringUtils::hasText).toArray();
        if (latInterval.length != 2) {
            throw new IllegalArgumentException(String.format("The wrong latitude interval parameter, %s", Arrays.toString(latInterval)));
        }
        int longStart = Integer.parseInt(longInterval[0].toString().trim());
        int longEnd = Integer.parseInt(longInterval[1].toString().trim());
        int[] longRange = new int[longEnd - longStart + 1];
        int longStep = longStart;
        for (int index = 0; index < longRange.length; index++) {
            longRange[index] = longStep;
            longStep++;
        }
        int latStart = latInterval[0].toString().charAt(0);
        int latEnd = latInterval[1].toString().trim().charAt(0);
        int[] latRange = new int[latEnd - latStart + 1];
        int latStep = latStart;
        for (int index = 0; index < latRange.length; index++) {
            latRange[index] = latStep;
            latStep++;
        }
        String[] instances = new String[longRange.length * latRange.length];
        int finalIndex = 0;
        if (longitudeFirst) {
            for (int i = 0; i < longRange.length; i++) {
                for (int i1 = 0; i1 < latRange.length; i1++) {
                    instances[finalIndex] = String.valueOf(longRange[i] + (char) latRange[i1]);
                    finalIndex++;
                }
            }
        } else {
            for (int i = 0; i < latRange.length; i++) {
                for (int i1 = 0; i1 < longRange.length; i1++) {
                    instances[finalIndex] = (char) latRange[i] + String.valueOf(longRange[i1]);
                    finalIndex++;
                }
            }
        }
        return Arrays.asList(instances);
    }

    public static void main(String[] args) {
        GridSet gridSet = new GridSet("42,53", "A,N");
        gridSet.instances().stream().forEach(System.out::println);
    }
}
