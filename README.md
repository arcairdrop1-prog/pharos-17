# Pharos Audit-Log Agent

An intelligent accounting and compliance agent developed in **Java** for the Pharos Agent Arena designed to generate plain-language activity reports for wallets over a chosen period[cite: 14].

## 📋 Requirements Met

1. **Wallet & Date Range Input (Req. 01):** Accepts a target wallet address and a specific date range filter[cite: 14].
2. **Activity Categorization (Req. 02):** Automatically groups transaction activity into transfers in and out, swaps, approvals, and contract interactions[cite: 14].
3. **Totals & Notable Transactions (Req. 03):** Includes financial totals per category alongside notable transactions with direct explorer links[cite: 14].
4. **Multi-Format Export (Req. 04):** Exports the generated audit report as Markdown, CSV, or structured layouts[cite: 14].

## 🚀 How to Run

1. Certifique-se de ter o **Java JDK** instalado.
2. Compile o ficheiro:
   ```bash
   javac AuditLogAgent.java
