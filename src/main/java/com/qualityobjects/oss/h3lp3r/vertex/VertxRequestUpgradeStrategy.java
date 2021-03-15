package com.qualityobjects.oss.h3lp3r.vertex;

import com.qualityobjects.oss.h3lp3r.vertex.utils.BufferConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;

public class VertxRequestUpgradeStrategy implements RequestUpgradeStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertxRequestUpgradeStrategy.class);

    private final BufferConverter bufferConverter;

    private final int maxWebSocketFrameSize;

    private final int maxWebSocketMessageSize;

    public VertxRequestUpgradeStrategy(int maxWebSocketFrameSize, int maxWebSocketMessageSize) {
        this.bufferConverter = new BufferConverter();
        this.maxWebSocketFrameSize = maxWebSocketFrameSize;
        this.maxWebSocketMessageSize = maxWebSocketMessageSize;
    }


}
