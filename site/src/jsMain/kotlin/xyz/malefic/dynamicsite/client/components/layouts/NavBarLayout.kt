package xyz.malefic.dynamicsite.client.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexGrow
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.dynamicsite.client.styles.ActiveNavItemStyle
import xyz.malefic.dynamicsite.client.styles.NavBarStyle
import xyz.malefic.dynamicsite.client.styles.NavItemHoverStyle
import xyz.malefic.dynamicsite.client.styles.isCurrentPage
import xyz.malefic.dynamicsite.client.util.Pages

@Layout
@Composable
fun NavBarLayout(content: @Composable () -> Unit) {
    val ctx = rememberPageContext()
    val currentRoute = ctx.route.path

    Column(Modifier.fillMaxWidth().height(100.vh)) {
        Box(
            NavBarStyle.toModifier(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .maxWidth(1200.px)
                    .padding(0.px, 20.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.flexGrow(1)) {
                    // Brand/Logo area (optional)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Pages.entries.forEach { page ->
                        val isActive = page.isCurrentPage(currentRoute)
                        val pageRoute = page.route

                        Link(
                            path = pageRoute,
                            modifier =
                                if (isActive) {
                                    ActiveNavItemStyle.toModifier()
                                } else {
                                    NavItemHoverStyle.toModifier()
                                },
                        ) {
                            Text(page.value)
                        }
                    }
                }
            }
        }

        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}
