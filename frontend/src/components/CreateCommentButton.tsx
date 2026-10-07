import { useState } from "react"
import { useApi } from "../api/apiFetch"
import type { Group } from "./GroupSelector"

type CreateCommentButtonProps = {
  selectedGroup: Group | null
  onCommentCreated: () => void
}

function CreateCommentButton({ selectedGroup, onCommentCreated }: CreateCommentButtonProps) {
  const [showModal, setShowModal] = useState(false)
  const [commentContent, setCommentContent] = useState("")
  const [loading, setLoading] = useState(false)

  const apiFetch = useApi()

  async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
    event.preventDefault()

    if (selectedGroup === null) {
      return
    }

    setLoading(true)

    try {
      const response = await apiFetch(
        `/api/groups/${selectedGroup.id}/comments`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            textSection: "Text",
            commentPageNumber: 1,
            commentPageOccurrence: 1,
            commentContent: commentContent
          })
        }
      )

      if (response?.ok) {
        onCommentCreated()
        setShowModal(false)
        setCommentContent("")
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <button
        type="button"
        className="create-comment-button"
        disabled={selectedGroup === null}
        onClick={() => setShowModal(true)}
      >
        Add Comment
      </button>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Add a comment</h2>

            <form onSubmit={handleSubmit}>
              <label htmlFor="commentContent">
                Comment
              </label>

              <textarea
                id="commentContent"
                value={commentContent}
                onChange={event =>
                  setCommentContent(event.target.value)
                }
                maxLength={100}
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
                  {loading ? "Adding..." : "Add"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  )
}

export default CreateCommentButton