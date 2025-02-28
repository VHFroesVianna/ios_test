//
//  Section.swift
//  dnmcForms
//
//  Created by Victor Hugo Froes on 2/25/25.
//


import Foundation

// MARK: - Section Model
struct Section: Identifiable, Codable {
    var id = UUID()
    let uuid: String
    let title: String
    let from: Int
    var to: Int
    let index: Int
    var fields: [Field]
}
