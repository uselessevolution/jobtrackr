import { useNavigate } from "react-router-dom";

import useAuth from "../auth/useAuth";

export default function HomePage() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate("/login", {
            replace: true,
        });
    }

    return (
        <main>
            <h1>JobTrackr</h1>

            <p>
                You are authenticated.
            </p>

            <button
                type="button"
                onClick={handleLogout}
            >
                Logout
            </button>
        </main>
    );
}