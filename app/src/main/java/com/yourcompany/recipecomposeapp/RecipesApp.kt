import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesApp() {
    RecipesAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Text(
                text = "Recipes App",
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}