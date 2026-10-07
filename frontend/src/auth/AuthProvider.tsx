import { useState, type ReactNode } from "react"
import { AuthContext } from "./AuthContext"

type AuthProviderProps = {
  children: ReactNode
}

function AuthProvider({ children }: AuthProviderProps) {
  const [accessToken, setAccessToken] = useState<string | null>(null)
  const [refreshToken, setRefreshToken] = useState<string | null>(null)

  return (
    <AuthContext.Provider
      value={{
        accessToken,
        refreshToken,
        setAccessToken,
        setRefreshToken
      }}
    >
        {children}
    </AuthContext.Provider>
  )
}

export default AuthProvider