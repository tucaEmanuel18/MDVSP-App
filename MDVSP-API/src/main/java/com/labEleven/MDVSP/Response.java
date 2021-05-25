package com.labEleven.MDVSP;

public class Response {

    private long status;
    private String content;

    public Response(long status, String content) {
        this.status = status;
        this.content = content;
    }

    public long getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}