
export type Comment = {
  commentId: number,
  commentPageNumber: number,
  commentContent: string,
  authorName: string,
  authorId: number
}

type CommentListItemProps = {
  comment: Comment
}

function CommentListItem({ comment }: CommentListItemProps) {
  return (
    <p className="comment">
      <strong>{comment.authorName}:</strong> {comment.commentContent}
    </p>
  )
}

export default CommentListItem