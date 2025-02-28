//
//  HTMLTextView.swift
//  dnmcForms
//
//  Created by Victor Hugo Froes on 2/25/25.
//


import SwiftUI
import WebKit

struct HTMLTextView: UIViewRepresentable {
    let html: String
    @State private var dynamicHeight: CGFloat = 10

    func makeUIView(context: Context) -> WKWebView {
        let webView = WKWebView()
        webView.navigationDelegate = context.coordinator

        let styledHTML = """
        <html>
        <head>
        <style>
            body { font-size: 22px; font-family: -apple-system, Helvetica, Arial, sans-serif; flex-direction: column; align-items: left; justify-content: center; }
            h1 { font-size: 65px; font-weight: bold; }
            h2 { font-size: 55px; }
            p { font-size: 50px; }
            img { max-height: 80px; width: auto; display: inline-block; }
        </style>
        </head>
        <body>\(html)</body>
        </html>
        """
        
        webView.loadHTMLString(styledHTML, baseURL: nil)
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, WKNavigationDelegate {
        var parent: HTMLTextView

        init(_ parent: HTMLTextView) {
            self.parent = parent
        }

        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
            webView.evaluateJavaScript("document.body.scrollHeight") { (height, error) in
                if let height = height as? CGFloat {
                    DispatchQueue.main.async {
                        self.parent.dynamicHeight = height
                    }
                }
            }
        }
    }
}
