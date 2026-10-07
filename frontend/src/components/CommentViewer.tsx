import { useEffect, useState } from "react"
import { useApi } from "../api/apiFetch"
import type { Group } from "./GroupSelector"
import CommentListItem from "./CommentListItem"
import type { Comment } from "./CommentListItem"

type CommentViewerProps = {
  selectedGroup: Group | null
  commentsRefresh: boolean
}

function CommentViewer({ selectedGroup, commentsRefresh }: CommentViewerProps) {
  const [comments, setComments] = useState<Comment[]>([])
  const apiFetch = useApi()

  useEffect(() => {
    async function loadComments() {


      if (selectedGroup === null) {
        setComments([])
        return
      }

      const response = await apiFetch(`/api/groups/${selectedGroup.id}/comments`)

      if (response) {
        const data = await response.json()
        setComments(data)
      }
    }

    loadComments()
  }, [selectedGroup, commentsRefresh])

  return (
    <div className="comment-viewer">
      <h3>Comments</h3>

      {comments.length === 0 ? (
        <div className="comments-empty">
          <p>No comments yet.</p>
        </div>
      ) : (
        <div className="comment-list">
          {comments.map(comment => (
            <CommentListItem
              key={comment.commentId}
              comment={comment}
            />
          ))}
        </div>
      )}
    </div>
  )
}

export default CommentViewer