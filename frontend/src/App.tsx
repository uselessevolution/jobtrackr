import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";
import CreateApplicationPage from "./pages/CreateApplicationPage";
import ProtectedRoute from "./auth/ProtectedRoute";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import ApplicationsPage from "./pages/ApplicationsPage";
function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <HomePage />
          </ProtectedRoute>
        }
      />

      <Route
        path="*"
        element={
          <Navigate
            to="/"
            replace
          />
        }
      />
      <Route
        path="/applications"
        element={
          <ProtectedRoute>
            <ApplicationsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/applications/new"
        element={
          <ProtectedRoute>
            <CreateApplicationPage />
          </ProtectedRoute>
        }
      />
    </Routes>

  );
}

export default App;