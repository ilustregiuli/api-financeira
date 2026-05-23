package com.giuli.api_financeira.security;

public class UserContext {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> empresaIdHolder = new ThreadLocal<>();

    public static void set(Long userId, Long empresaId) {
        userIdHolder.set(userId);
        empresaIdHolder.set(empresaId);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static Long getEmpresaId() {
        return empresaIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
        empresaIdHolder.remove();
    }
}
