package org.fuyi.wukong.core.command;

/**
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 6/8/2022 4:53 pm
 * @since: 1.0
 **/
public enum TransformStatus {

    OK(200, "OK"),
    INIT(0, "INIT"),
    WORKING(1, "WORKING"),
    ERROR(500, "ERROR");

    private final int value;
    private final String reasonPhrase;

    private TransformStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
