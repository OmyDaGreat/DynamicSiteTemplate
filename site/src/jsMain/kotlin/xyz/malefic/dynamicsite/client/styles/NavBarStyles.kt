package xyz.malefic.dynamicsite.client.styles

import com.varabyte.kobweb.compose.css.Background
import com.varabyte.kobweb.compose.css.BackgroundImage
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.textDecoration
import xyz.malefic.dynamicsite.client.util.Pages
import xyz.malefic.kutint.parseHex
import xyz.malefic.kutint.rgba

val NavBarStyle =
    CssStyle.base {
        Modifier
            .fillMaxWidth()
            .height(60.px)
            .background(
                Background.of(
                    BackgroundImage.of(
                        linearGradient(
                            LinearGradient.Direction.ToRight,
                        ) {
                            add(parseHex("#f8f9fa"), 0.percent)
                            add(parseHex("#e9ecef"), 50.percent)
                            add(parseHex("#dee2e6"), 100.percent)
                        },
                    ),
                ),
            ).boxShadow(0.px, 2.px, 4.px, color = rgba(0, 0, 0, 0.1f))
            .borderBottom(1.px, LineStyle.Solid, parseHex("#dee2e6"))
    }

val NavItemStyle =
    Modifier
        .padding(12.px, 20.px)
        .margin(0.px, 4.px)
        .borderRadius(6.px)
        .styleModifier {
            textDecoration("none")
        }.color(parseHex("#495057"))
        .fontSize(14.px)
        .fontWeight(500)
        .transition(Transition.all(0.2.s))
        .whiteSpace(WhiteSpace.NoWrap)

val NavItemHoverStyle =
    CssStyle {
        base {
            NavItemStyle
        }

        hover {
            Modifier
                .background(rgba(108, 117, 125, 0.1f))
                .color(parseHex("#212529"))
                .translateY((-1).px)
        }
    }

val ActiveNavItemStyle =
    CssStyle.base {
        NavItemStyle
            .background(rgba(13, 110, 253, 0.1f))
            .color(parseHex("#0d6efd"))
            .fontWeight(600)
    }

fun Pages.isCurrentPage(currentRoute: String): Boolean =
    when (this) {
        Pages.INDEX -> currentRoute == "" || currentRoute == "/"
        else -> currentRoute == route
    }
