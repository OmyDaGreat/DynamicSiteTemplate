package xyz.malefic.staticsite.styles

import com.varabyte.kobweb.compose.css.Background
import com.varabyte.kobweb.compose.css.BackgroundImage
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.selectors.hover
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.textDecoration
import xyz.malefic.kutint.parseHex
import xyz.malefic.kutint.rgba
import xyz.malefic.staticsite.util.Pages

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

val DropdownStyle =
    CssStyle.base {
        Modifier
            .position(Position.Relative)
            .display(DisplayStyle.InlineBlock)
    }

val DropdownContentStyle =
    CssStyle.base {
        Modifier
            .position(Position.Absolute)
            .top(100.percent)
            .right(0.px)
            .background(Colors.White)
            .minWidth(180.px)
            .boxShadow(0.px, 8.px, 16.px, color = rgba(0, 0, 0, 0.15f))
            .borderRadius(8.px)
            .border(1.px, LineStyle.Solid, parseHex("#dee2e6"))
            .zIndex(1000)
            .padding(8.px, 0.px)
    }

val DropdownItemStyle =
    Modifier
        .display(DisplayStyle.Block)
        .padding(10.px, 16.px)
        .styleModifier {
            textDecoration("none")
        }.color(parseHex("#495057"))
        .fontSize(14.px)
        .transition(Transition.of("background-color", 0.15.s))
        .whiteSpace(WhiteSpace.NoWrap)

val DropdownItemHoverStyle =
    CssStyle {
        base {
            DropdownItemStyle
        }

        hover {
            Modifier.background(parseHex("#f8f9fa"))
        }
    }

val DropdownButtonHoverStyle =
    CssStyle {
        base {
            Modifier
                .padding(12.px, 16.px)
                .margin(0.px, 4.px)
                .borderRadius(6.px)
                .background(Colors.Transparent)
                .border(1.px, LineStyle.Solid, parseHex("#dee2e6"))
                .color(parseHex("#495057"))
                .fontSize(14.px)
                .fontWeight(500)
                .cursor(Cursor.Pointer)
                .transition(Transition.all(0.2.s))
                .whiteSpace(WhiteSpace.NoWrap)
        }

        hover {
            Modifier
                .background(rgba(108, 117, 125, 0.1f))
                .border {
                    color(parseHex("#adb5bd"))
                }
        }
    }

fun Pages.isCurrentPage(currentRoute: String): Boolean =
    when (this) {
        Pages.INDEX -> currentRoute == "" || currentRoute == "/"
        else -> currentRoute == route
    }
