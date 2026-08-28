import {
  Navigate,
  Route,
  Routes,
} from "react-router-dom";

import ProtectedRoute from "./auth/ProtectedRoute";
import AppLayout from "./components/AppLayout";

import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import ApplicationsPage from "./pages/ApplicationsPage";
import CreateApplicationPage from "./pages/CreateApplicationPage";
import ApplicationDetailPage from "./pages/ApplicationDetailPage";
import EditApplicationPage from "./pages/EditApplicationPage";
import DashboardPage from "./pages/DashboardPage";
import RemindersPage from "./pages/RemindersPage";

function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route
          path="/"
          element={<HomePage />}
        />

        <Route
          path="/dashboard"
          element={<DashboardPage />}
        />

        <Route
          path="/applications"
          element={<ApplicationsPage />}
        />

        <Route
          path="/applications/new"
          element={<CreateApplicationPage />}
        />

        <Route
          path="/applications/:id"
          element={<ApplicationDetailPage />}
        />

        <Route
          path="/applications/:id/edit"
          element={<EditApplicationPage />}
        />

        <Route
          path="/reminders"
          element={<RemindersPage />}
        />
      </Route>

      <Route
        path="*"
        element={
          <Navigate
            to="/"
            replace
          />
        }
      />
    </Routes>
  );
}

export default App;