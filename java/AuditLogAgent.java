import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuditLogAgent {

    public static class Transaction {
        String txHash;
        String category; // "Transfers In/Out", "Swaps", "Approvals", "Contract Interactions"[cite: 14]
        double valueUSD;
        LocalDateTime timestamp;
        String explorerLink;

        public Transaction(String txHash, String category, double valueUSD, LocalDateTime timestamp, String explorerLink) {
            this.txHash = txHash;
            this.category = category;
            this.valueUSD = valueUSD;
            this.timestamp = timestamp;
            this.explorerLink = explorerLink;
        }
    }

    public static class AuditReport {
        String walletAddress;
        LocalDate startDate;
        LocalDate endDate;
        List<Transaction> transactions;

        public AuditReport(String walletAddress, LocalDate startDate, LocalDate endDate, List<Transaction> transactions) {
            this.walletAddress = walletAddress;
            this.startDate = startDate;
            this.endDate = endDate;
            this.transactions = transactions;
        }

        // Requirement 04: Exports report as Markdown[cite: 14]
        public String exportAsMarkdown() {
            StringBuilder sb = new StringBuilder();
            sb.append("# Audit Log Report\n");
            sb.append(String.format("- **Wallet Address:** %s\n", walletAddress));
            sb.append(String.format("- **Date Range:** %s to %s\n\n", startDate, endDate));
            
            // Requirement 03: Totals per category[cite: 14]
            sb.append("## Totals per Category\n");
            Map<String, Double> totals = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.category, Collectors.summingDouble(t -> t.valueUSD)));
            
            totals.forEach((cat, total) -> sb.append(String.format("- **%s:** $%.2f USD\n", cat, total)));

            // Requirement 03: Notable transactions with links[cite: 14]
            sb.append("\n## Notable Transactions & Links\n");
            for (Transaction t : transactions) {
                sb.append(String.format("- [%s] **%s**: $%.2f ([Explorer Link](%s))\n", t.timestamp, t.category, t.valueUSD, t.explorerLink));
            }
            return sb.toString();
        }

        // Requirement 04: Exports report as CSV[cite: 14]
        public String exportAsCSV() {
            StringBuilder sb = new StringBuilder();
            sb.append("TxHash,Category,ValueUSD,Timestamp,ExplorerLink\n");
            for (Transaction t : transactions) {
                sb.append(String.format("%s,%s,%.2f,%s,%s\n", t.txHash, t.category, t.valueUSD, t.timestamp, t.explorerLink));
            }
            return sb.toString();
        }
    }

    // Requirement 01: Accepts a wallet address and a date range[cite: 14]
    public AuditReport generateReport(String walletAddress, LocalDate startDate, LocalDate endDate, List<Transaction> allTxs) {
        List<Transaction> filtered = allTxs.stream()
            .filter(t -> !t.timestamp.toLocalDate().isBefore(startDate) && !t.timestamp.toLocalDate().isAfter(endDate))
            .collect(Collectors.toList());

        return new AuditReport(walletAddress, startDate, endDate, filtered);
    }

    public static void main(String[] args) {
        AuditLogAgent agent = new AuditLogAgent();

        List<Transaction> sampleTxs = new ArrayList<>();
        // Requirement 02: Grouped into categories[cite: 14]
        sampleTxs.add(new Transaction("0x111...", "Transfers In/Out", 1500.0, LocalDateTime.of(2026, 10, 1, 10, 0), "https://explorer.ai.pharos.xyz/tx/0x111"));
        sampleTxs.add(new Transaction("0x222...", "Swaps", 450.0, LocalDateTime.of(2026, 10, 5, 14, 30), "https://explorer.ai.pharos.xyz/tx/0x222"));
        sampleTxs.add(new Transaction("0x333...", "Approvals", 0.0, LocalDateTime.of(2026, 10, 10, 9, 15), "https://explorer.ai.pharos.xyz/tx/0x333"));
        sampleTxs.add(new Transaction("0x444...", "Contract Interactions", 2500.0, LocalDateTime.of(2026, 10, 20, 18, 45), "https://explorer.ai.pharos.xyz/tx/0x444"));

        LocalDate start = LocalDate.of(2026, 10, 1);
        LocalDate end = LocalDate.of(2026, 10, 31);

        AuditReport report = agent.generateReport("0xUserWallet789...", start, end, sampleTxs);

        System.out.println("=== EXPORTING REPORT AS MARKDOWN ===");
        System.out.println(report.exportAsMarkdown());

        System.out.println("\n=== EXPORTING REPORT AS CSV ===");
        System.out.println(report.exportAsCSV());
    }
}
