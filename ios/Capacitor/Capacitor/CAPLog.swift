public class CAPLog {
    public static var enableLogging: Bool = true

    private static var history: [String] = []
    private static let historyLimit = 3000
    private static let historyQueue = DispatchQueue(label: "CAPLogHistory",
                                                    attributes: .concurrent)

    public static func print(_ items: Any..., separator: String = " ", terminator: String = "\n") {
        guard enableLogging else { return }

        for (index, item) in items.enumerated() {
            let str = "\(item)".prefix(4068)
            Swift.print(str, terminator: index == items.count - 1 ? terminator : separator)

            historyQueue.async(flags: .barrier) {
                history.append(String(str))
                if history.count > historyLimit {
                    history.removeFirst(history.count - historyLimit)
                }
            }
        }
    }

    public static func getHistory(max: Int? = nil) -> [String] {
        historyQueue.sync {
            if let m = max {
                return Array(history.suffix(m))
            }
            return history
        }
    }

    public static func clearHistory() {
        historyQueue.async(flags: .barrier) {
            history.removeAll()
        }
    }
}

