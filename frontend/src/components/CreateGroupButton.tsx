import { useState } from "react"
import { useApi } from "../api/apiFetch"

type CreateGroupButtonProps = {
  onGroupCreated: () => void
}

function CreateGroupButton({ onGroupCreated }: CreateGroupButtonProps) {
  const [showModal, setShowModal] = useState(false)
  const [groupName, setGroupName] = useState("")
  const [readingText, setReadingText] = useState<File | null>(null)
  const [loading, setLoading] = useState(false)

  const apiFetch = useApi()

  async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault()

    if (readingText === null) {
      return
    }

    const formData = new FormData()

    formData.append("groupName", groupName)
    formData.append("readingText", readingText)

    setLoading(true)

    try {
      const response = await apiFetch("/api/groups", {
        method: "POST",
        body: formData
      })

      if (response?.ok) {
        onGroupCreated()
        setShowModal(false)
        setGroupName("")
        setReadingText(null)
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <button
        type="button"
        className="create-group-button"
        onClick={() => setShowModal(true)}
      >
        Create Group
      </button>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Create a group</h2>

            <form onSubmit={handleSubmit}>
              <label htmlFor="groupName">Group name</label>

              <input
                id="groupName"
                type="text"
                value={groupName}
                onChange={event => setGroupName(event.target.value)}
                required
              />

              <label htmlFor="readingText">Reading text</label>

              <input
                id="readingText"
                type="file"
                accept="application/pdf"
                onChange={event =>
                  setReadingText(event.target.files?.[0] ?? null)
                }
                required
              />

              <div className="modal-actions">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>

                <button type="submit" disabled={loading}>
                  {loading ? "Creating..." : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  )
}

export default CreateGroupButton