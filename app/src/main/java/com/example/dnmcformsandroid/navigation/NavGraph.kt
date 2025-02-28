import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.dnmcformsandroid.controllers.JSONLoader
import com.example.dnmcformsandroid.views.*


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "sectionList") {
        composable("sectionList") {
            SectionListScreen(navController)
        }
        composable("sectionDetail/{sectionId}") { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId") ?: return@composable
            val context = LocalContext.current
            val sections = JSONLoader.loadSections(context)  // ✅ Load sections from JSON
            val section = sections.find { it.uuid == sectionId }  // ✅ Find section by UUID

            if (section != null) {
                SectionDetailScreen(navController, section.uuid)  // ✅ Now passing a Section, not a String
            } else {
                println("❌ Error: Section with ID $sectionId not found")
            }
        }
        composable("addField/{sectionId}") { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId") ?: return@composable
            AddFieldScreen(navController, sectionId)
        }
    }
}

