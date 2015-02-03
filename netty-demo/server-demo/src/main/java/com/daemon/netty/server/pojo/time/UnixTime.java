package com.daemon.netty.server.pojo.time;

import java.util.Date;

/**
 * Created by daemon on 15-2-2.
 */
public class UnixTime {

    private final long time;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(final long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return new Date((time - 2208988800L) * 1000L).toString();
    }
}
