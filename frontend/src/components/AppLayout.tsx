import {
    Link,
    Outlet,
    useNavigate,
} from "react-router-dom";

import useAuth from "../auth/useAuth";

export default function AppLayout() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();

        navigate("/login", {
            replace: true,
        });
    }

    return (
        <div className="app-shell">
            <header className="app-header">
                <div className="app-header-content">
                    <Link
                        to="/"
                        className="brand"
                    >
                        JobTrackr
                    </Link>

                    <nav className="main-nav">
                        <Link to="/dashboard">
                            Dashboard
                        </Link>

                        <Link to="/applications">
                            Applications
                        </Link>

                        <Link to="/reminders">
                            Reminders
                        </Link>
                    </nav>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                </div>
            </header>

            <div className="page-container">
                <Outlet />
            </div>
        </div>
    );
}