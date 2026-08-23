export default function HomePage() {
    return (
        <main>
            <h1>JobTrackr</h1>
            <p>Track your job applications in one place.</p>
            <p>API: {import.meta.env.VITE_API_BASE_URL}</p>
        </main>
    );
}
