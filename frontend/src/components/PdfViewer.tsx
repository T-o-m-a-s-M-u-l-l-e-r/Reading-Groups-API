import { useEffect, useState } from "react"
import type { Group } from "./GroupSelector"
import { useApi } from "../api/apiFetch"

type PdfViewerProps = {
  selectedGroup: Group | null
}

function PdfViewer({ selectedGroup }: PdfViewerProps) {
  const [pdfUrl, setPdfUrl] = useState<string | null>(null)
  const apiFetch = useApi()

  useEffect(() => {

    if (selectedGroup == null) {
      return
    }

    const groupId = selectedGroup.id
    let url: string | null = null

    async function loadPdf() {
      const response = await apiFetch(
        `/api/groups/${groupId}/reading-text`
      )

      if (response) {
        const data = await response.blob()

        url = URL.createObjectURL(data)
        setPdfUrl(url)
      }
    }

    loadPdf()

    return () => {
      if (url !== null) {
        URL.revokeObjectURL(url)
      }
    }
  }, [selectedGroup])

  if (selectedGroup === null) {
    return (
      <div className="pdf-empty">
        <p>[Select a group to start reading]</p>
      </div>
    )
  }

  return (
    <div className="pdf-viewer">
      {pdfUrl && (
        <iframe
          src={pdfUrl}
          title="Reading text"
        />
      )}
    </div>
  )
}

export default PdfViewer