import { useContext } from "react"
import { AuthContext } from "../auth/AuthContext"
import AnonymousHome from "../components/AnonymousHome"
import UserHome from "../components/UserHome"

function HomePage() {
  const auth = useContext(AuthContext)

  if (auth === null) {
    console.log("Auth object is null")
    return
  }

  return (
    <main>
      {auth.accessToken === null || auth.refreshToken === null
        ? <AnonymousHome />
        : <UserHome />
      }
    </main>
  )
}

export default HomePage
