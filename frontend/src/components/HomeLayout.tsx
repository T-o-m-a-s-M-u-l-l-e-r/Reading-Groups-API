import { Link } from 'react-router-dom'
import './AnonymousHome.css'
import type { ReactNode } from 'react'

type HomeLayoutProps = {
  children: ReactNode
}

function HomeLayout({ children }: HomeLayoutProps) {
  return (
    <main className="home">
      <div className="page-content">
        <section className="hero">
          <Link to="/" className="site-title">
            Reading Groups
          </Link>
          {children}
        </section>

        <section className="project-info">
          <p>
            Built by <strong>Tomáš Müller</strong>
          </p>

          <p className="project-description">
            A full-stack portfolio project built with React,
            Spring Boot and PostgreSQL.
          </p>

          <nav className="project-links">
            <a href="https://github.com/T-o-m-a-s-M-u-l-l-e-r/Reading-Groups-API">GitHub</a>
            <Link to="/about">About</Link>
          </nav>
        </section>
      </div>
    </main>
  )
}

export default HomeLayout