package upload

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import java.awt.FileDialog

@Composable
fun UploadDialog(onClose: () -> Unit = {}, onUpload: (List<UploadedFile>) -> Unit) {
    val (visibility, setVisibility) = remember { mutableStateOf(true) }
    return with(
        FileDialog(
            ComposeWindow(),
            "Upload files",
            FileDialog.LOAD
        ).apply {
            isAlwaysOnTop = true
            isMultipleMode = true
            isVisible = visibility
        }
    ) {
        try {
            onUpload(files.map { UploadedFile(it.nameWithoutExtension, it.extension, it.readBytes()) })
        } catch (e: Exception) {
            println("Upload dialog exception: ${e.localizedMessage}")
        } finally {
            setVisibility(false)
            onClose()
        }
    }
}