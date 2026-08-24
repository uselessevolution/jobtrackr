import {
    useMemo,
    useState,
    type ReactNode,
} from "react";

import { login as loginRequest } from "../api/authApi";
import {
    clearAccessToken,
    getAccessToken,
    setAccessToken,
} from "./tokenStorage";
import {
    AuthContext,
    type AuthContextValue,
} from "./AuthContext";

interface AuthProviderProps {
    children: ReactNode;
}

export default function AuthProvider({
    children,
}: AuthProviderProps) {
    const [token, setToken] =
        useState<string | null>(
            () => getAccessToken(),
        );

    async function login(
        email: string,
        password: string,
    ): Promise<void> {
        const response =
            await loginRequest({
                email,
                password,
            });

        setAccessToken(
            response.accessToken,
        );

        setToken(
            response.accessToken,
        );
    }

    function logout(): void {
        clearAccessToken();
        setToken(null);
    }

    const value =
        useMemo<AuthContextValue>(
            () => ({
                token,
                isAuthenticated:
                    token !== null,
                login,
                logout,
            }),
            [token],
        );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}