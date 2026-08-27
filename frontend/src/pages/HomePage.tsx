import { useNavigate } from "react-router-dom";

import useAuth from "../auth/useAuth";
import {
    Link,
} from "react-router-dom";
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
            <p>
                <Link to="/applications">
                    View Applications
                </Link>
            </p>
            <p>
                <Link to="/dashboard">
                    View Dashboard
                </Link>
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