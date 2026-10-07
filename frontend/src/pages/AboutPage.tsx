import HomeLayout from "../components/HomeLayout"

function AboutPage() {
  return (
    <HomeLayout>
      <div className="about">
        <h2>About Reading Groups</h2>

        <p>
          Reading Groups is an ongoing full-stack web application project. The platform is designed for a reading group experience; for people who want to read, interpret and discuss the same text together.
        </p>
        <br></br>

        <p>
          The project spans the full application lifecycle, including frontend and backend development, database design, authentication and authorization, containerization, CI/CD, and deployment.
        </p>

        <h3>Technology</h3>

        <p>
          The backend is built with Java and Spring Boot and uses PostgreSQL for persistence and JWT-based authentication. The frontend is built with React and TypeScript.

          The application is containerized with Docker and deployed using Docker Compose and Caddy. GitHub Actions provides the CI/CD pipeline, with application images published to GHCR and deployed to a VPS.
        </p>
      </div>
    </HomeLayout>
  )
}

export default AboutPage