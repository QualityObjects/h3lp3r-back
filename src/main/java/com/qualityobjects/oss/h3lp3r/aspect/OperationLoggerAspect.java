package com.qualityobjects.oss.h3lp3r.aspect;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.qualityobjects.oss.h3lp3r.controller.RootController;
import com.qualityobjects.oss.h3lp3r.domain.document.OperationLog;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Aspect
@Component
@Slf4j
public class OperationLoggerAspect {

    @Autowired
    OperationLogRepository olRepository;

    @Pointcut("execution(public reactor.core.publisher.Mono<com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse> com.qualityobjects.oss.h3lp3r.service.*.*(..)) && args(input)")
    public void operations(OpInput input) {
    }

    @Around("operations(input)")
    @SuppressWarnings("unchecked")
    public Mono<OpResponse> aroundOperation(ProceedingJoinPoint pjp, OpInput input) throws Throwable {
        final Mono<OpResponse> r = Mono.class.cast(pjp.proceed());

        return Mono.deferContextual(ctx -> {
            ServerHttpRequest req = ctx.get(ServerHttpRequest.class);
            return r.doOnSuccess(ok -> {
                logOperation(input, req);
            }).doOnError(ex -> {
                logOperation(input, req, ex.toString());
            });
        });
    }

    private void logOperation(OpInput input, ServerHttpRequest request) {
        logOperation(input, request, null);
    }

    private void logOperation(OpInput input, ServerHttpRequest request, @Nullable String errorMessage) {
//        log.info("Logging operation: {}", input.getAction());
        LocalDateTime before = LocalDateTime.now();
        String remoteIp = RootController.getRealIp(request);
        String userAgent = request.getHeaders().getFirst("user-agent");
        Long duration = Duration.between(before, LocalDateTime.now()).toNanos();
        LocalDateTime opTs = before.atZone(ZoneOffset.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        OperationLog op = OperationLog.builder().duration(duration) //
                                        .clientIp(remoteIp) //
                                        .operationTimestamp(opTs) //
                                        .operation(input.getAction()) //
                                        .params(input.getParams().entrySet().stream().map(entry -> Map.entry(entry.getKey(), entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))) //
                                        .userAgent(userAgent).build();
        if (errorMessage != null) {
            op.setSuccess(false);
            op.setErrorMsg(errorMessage);
        }
        
        executor.execute(() -> {
            olRepository.save(op)
            .doOnError(ex -> {
                log.error("Error saving ES operation: " + ex);
            }).block();
        });
        
    }


    
    Executor executor = Executors.newFixedThreadPool(8);
}
