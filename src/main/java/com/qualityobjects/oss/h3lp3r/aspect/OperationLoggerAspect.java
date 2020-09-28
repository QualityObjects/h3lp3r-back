package com.qualityobjects.oss.h3lp3r.aspect;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.qualityobjects.oss.h3lp3r.controller.RootController;
import com.qualityobjects.oss.h3lp3r.domain.document.OperationLog;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.repository.OperationLogRepository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Aspect
@Component
public class OperationLoggerAspect {

    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(OperationLoggerAspect.class);

    @Autowired
    OperationLogRepository olRepository;

    private @Autowired HttpServletRequest request;

    @Pointcut("execution(public com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse com.qualityobjects.oss.h3lp3r.service.*.*(..)) && args(input)")
    public void operations(OpInput input) {
    }

    @Around("operations(input)")
    public OpResponse aroundOperation(ProceedingJoinPoint pjp, OpInput input) throws Throwable {
        LocalDateTime before = LocalDateTime.now();
        String errorMsg = null;
        try {
            return (OpResponse)pjp.proceed();
        } catch (Exception | Error ex) {
            errorMsg = ex.toString();
            throw ex;
        } finally {
            String remoteIp = RootController.getRealIp(request);
            Long duration = Duration.between(before, LocalDateTime.now()).toNanos();
            OperationLog op = OperationLog.builder().duration(duration) //
                                            .clientIp(remoteIp) //
                                            .operationTimestamp(before) //
                                            .action(input.getAction()) //
                                            .params(input.getParams().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))) //
                                            .userAgent(request.getHeader("user-agent")).build();
            if (errorMsg != null) {
                op.setSuccess(false);
                op.setErrorMsg(errorMsg);
            }
            olRepository.save(op);
        }
    }

}
