import { createContext } from 'react'

type AuthContextType = {
  accessToken: string | null
  refreshToken: string | null
  setAccessToken: (token: string | null) => void
  setRefreshToken: (token: string | null) => void
}

export const AuthContext = createContext<AuthContextType | null>(null)