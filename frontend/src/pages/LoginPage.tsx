import {
    useState,
    type FormEvent,
} from "react";
import { Navigate, useNavigate } from "react-router-dom";
import axios from "axios";

import useAuth from "../auth/useAuth";

export default function LoginPage() {
    const {
        login,
        isAuthenticated,
    } = useAuth();

    const navigate = useNavigate();

    const [email, setEmail] =
        useState("");

    const [password, setPassword] =
        useState("");

    const [error, setError] =
        useState("");

    const [isSubmitting, setIsSubmitting] =
        useState(false);

    if (isAuthenticated) {
        return <Navigate to="/" replace />;
    }

    async function handleSubmit(
        event: FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        setError("");
        setIsSubmitting(true);

        try {
            await login(email, password);

            navigate("/", {
                replace: true,
            });
        } catch (error) {
            if (axios.isAxiosError(error)) {
                const message =
                    error.response?.data?.message;

                setError(
                    typeof message === "string"
                        ? message
                        : "Login failed. Please check your credentials.",
                );
            } else {
                setError(
                    "Login failed. Please try again.",
                );
            }
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <main>
            <h1>Login</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="email">
                        Email
                    </label>

                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(event) =>
                            setEmail(event.target.value)
                        }
                        required
                        autoComplete="email"
                    />
                </div>

                <div>
                    <label htmlFor="password">
                        Password
                    </label>

                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(event) =>
                            setPassword(event.target.value)
                        }
                        required
                        autoComplete="current-password"
                    />
                </div>

                {error && (
                    <p role="alert">
                        {error}
                    </p>
                )}

                <button
                    type="submit"
                    disabled={isSubmitting}
                >
                    {isSubmitting
                        ? "Logging in..."
                        : "Login"}
                </button>
            </form>
        </main>
    );
}