import { Link } from 'react-router-dom'
import './AnonymousHome.css'
import HomeLayout from './HomeLayout'

function AnonymousHome() {
  return (
    <HomeLayout>
      <main>
        <h2>Read. Discuss. Grow.</h2>

        <p>
          Create reading groups, share documents,
          and discuss what you're reading.
        </p>

        <div className="hero-actions">
          <Link to="/login" className="button button-secondary">
            Login
          </Link>

          <Link to="/register" className="button button-primary">
            Register
          </Link>
        </div>
      </main>
    </HomeLayout>
  )
}

export default AnonymousHome