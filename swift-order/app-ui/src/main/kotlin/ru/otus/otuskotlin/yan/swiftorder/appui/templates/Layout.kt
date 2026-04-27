package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*

fun HTML.layout(
    title: String,
    transport: String,
    error: String? = null,
    content: FlowContent.() -> Unit,
) {
    head {
        title(title)
        meta(charset = "UTF-8")
        style {
            unsafe {
                +"""
                * { box-sizing: border-box; margin: 0; padding: 0; }
                body { background: #f5f5f5; font-family: sans-serif; font-size: 15px; }
                header { background: #2c3e50; color: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; }
                header a { color: white; text-decoration: none; font-size: 1.15em; font-weight: bold; }
                .transport-switcher { display: flex; gap: 8px; align-items: center; }
                .transport-switcher span { color: #aaa; font-size: 0.85em; margin-right: 4px; }
                .transport-btn { padding: 5px 14px; border: 1px solid #aaa; color: #ccc; background: transparent; cursor: pointer; border-radius: 4px; text-decoration: none; font-size: 0.85em; }
                .transport-btn.active { background: white; color: #2c3e50; border-color: white; font-weight: 600; }
                .error-banner { background: #e74c3c; color: white; padding: 10px 24px; font-size: 0.9em; }
                .main { max-width: 960px; margin: 28px auto; padding: 0 16px; }
                .card { background: white; border-radius: 6px; padding: 24px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
                h1 { font-size: 1.25em; margin-bottom: 18px; color: #2c3e50; }
                .row-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px; }
                table { width: 100%; border-collapse: collapse; }
                th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #eee; }
                th { background: #f8f8f8; font-weight: 600; color: #555; font-size: 0.9em; }
                tr:hover td { background: #f0f4f8; }
                .btn { display: inline-block; padding: 7px 16px; border-radius: 4px; text-decoration: none; font-size: 0.88em; cursor: pointer; border: none; font-family: inherit; }
                .btn-primary { background: #3498db; color: white; }
                .btn-primary:hover { background: #2980b9; }
                .btn-danger { background: #e74c3c; color: white; }
                .btn-danger:hover { background: #c0392b; }
                .btn-secondary { background: #95a5a6; color: white; }
                .field-row { display: grid; grid-template-columns: 140px 1fr; gap: 10px; padding: 8px 0; border-bottom: 1px solid #f0f0f0; align-items: center; }
                .field-label { font-weight: 500; color: #666; font-size: 0.9em; }
                form label { display: block; margin-bottom: 4px; font-weight: 500; color: #555; font-size: 0.88em; }
                form input, form select { width: 100%; padding: 8px 10px; margin-bottom: 14px; border: 1px solid #ddd; border-radius: 4px; font-size: 0.95em; font-family: inherit; }
                .actions { display: flex; gap: 10px; margin-top: 16px; }
                .back-link { display: inline-block; margin-bottom: 16px; color: #3498db; text-decoration: none; font-size: 0.9em; }
                .empty-state { color: #999; text-align: center; padding: 32px 0; }
                """.trimIndent()
            }
        }
    }
    body {
        header {
            a(href = "/orders?transport=$transport") { +"SwiftOrder" }
            div("transport-switcher") {
                span { +"Транспорт:" }
                a(href = "?transport=http",
                    classes = "transport-btn${if (transport == "http") " active" else ""}") { +"HTTP" }
                a(href = "?transport=kafka",
                    classes = "transport-btn${if (transport == "kafka") " active" else ""}") { +"Kafka" }
            }
        }
        if (error != null) {
            div("error-banner") { +error }
        }
        div(classes = "main") { content() }
    }
}
