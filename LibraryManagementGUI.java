// All necessary imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LibraryManagementGUI extends JFrame {
    // Declare all instance variables properly
    private JTabbedPane tabbedPane;
    private DefaultTableModel bookTableModel;
    private DefaultTableModel borrowTableModel;
    private JTable bookTable;
    private JTable borrowTable;
    
    // Data collections - properly declared
    private List<Book> books;
    private List<BorrowRecord> borrowRecords;
    
    // GUI Components - declared as instance variables
    private JTextField bookSearchField;
    private JTextField memberIdField;
    private JTextField bookIdField;
    private JTextField borrowDateField;
    private JTextField dueDateField;
    private JTextField returnMemberIdField;
    private JTextField returnBookIdField;
    private JTextField returnDateField;
    private JTextField fineMemberIdField;
    private JTextArea fineResultArea;
    
    public LibraryManagementGUI() {
        initializeData();
        initializeGUI();
    }
    
    private void initializeData() {
        // Initialize collections
        books = new ArrayList<>();
        borrowRecords = new ArrayList<>();
        
        // Add sample data
        books.add(new Book("1", "Java Programming", "John Smith", "978-1234567890", true));
        books.add(new Book("2", "Web Development", "Jane Doe", "978-0987654321", false));
        books.add(new Book("3", "Database Systems", "Bob Johnson", "978-1122334455", true));
        books.add(new Book("4", "Data Structures", "Alice Brown", "978-5566778899", true));
        books.add(new Book("5", "Software Engineering", "Charlie Wilson", "978-9988776655", false));
        
        // Add sample borrow records
        borrowRecords.add(new BorrowRecord("M001", "2", "Web Development", "2025-09-15", "2025-10-15"));
        borrowRecords.add(new BorrowRecord("M002", "5", "Software Engineering", "2025-09-20", "2025-10-20"));
    }
    
    private void initializeGUI() {
        // Set up main window
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create and configure tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add all tabs
        tabbedPane.addTab("📚 Book Search", createBookSearchPanel());
        tabbedPane.addTab("📖 Borrow Book", createBorrowPanel());
        tabbedPane.addTab("🔄 Return Book", createReturnPanel());
        tabbedPane.addTab("💰 Fine Calculator", createFinePanel());
        
        // Add tabbed pane to main window
        add(tabbedPane, BorderLayout.CENTER);
        
        // Configure window properties
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createBookSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("📚 Book Search & Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));
        
        // Initialize search field
        bookSearchField = new JTextField(20);
        JButton searchButton = new JButton("🔍 Search");
        JButton clearButton = new JButton("🔄 Show All");
        
        // Add action listeners
        searchButton.addActionListener(e -> performBookSearch());
        clearButton.addActionListener(e -> {
            bookSearchField.setText("");
            updateBookTable("");
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(bookSearchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        // Create table
        String[] columnNames = {"ID", "Title", "Author", "ISBN", "Status"};
        bookTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Books"));
        
        // Load initial data
        updateBookTable("");
        
        // Arrange components
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createBorrowPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("📖 Borrow Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Borrowing Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialize form fields
        memberIdField = new JTextField(15);
        bookIdField = new JTextField(15);
        borrowDateField = new JTextField(15);
        dueDateField = new JTextField(15);
        
        // Set default dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        borrowDateField.setText(dateFormat.format(today));
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        dueDateField.setText(dateFormat.format(calendar.getTime()));
        
        // Create form layout
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(memberIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(bookIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Borrow Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(borrowDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dueDateField, gbc);
        
        // Buttons
        JButton borrowButton = new JButton("📖 Borrow Book");
        JButton clearButton = new JButton("🗑️ Clear Form");
        
        borrowButton.addActionListener(new BorrowBookActionListener());
        clearButton.addActionListener(e -> clearBorrowForm());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(borrowButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);
        
        // Instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JTextArea instructions = new JTextArea(
            "Instructions:\n" +
            "1. Enter the Member ID (e.g., M001, M002)\n" +
            "2. Enter the Book ID (check Book Search tab for available books)\n" +
            "3. Borrow date is set to today by default\n" +
            "4. Due date is set to 30 days from today by default\n" +
            "5. Click 'Borrow Book' to complete the transaction"
        );
        instructions.setEditable(false);
        instructions.setBackground(getBackground());
        instructions.setFont(new Font("Arial", Font.PLAIN, 11));
        instructionsPanel.add(instructions, BorderLayout.CENTER);
        
        // Arrange main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(instructionsPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createReturnPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("🔄 Return Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        // Return form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Return Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialize return form fields
        returnMemberIdField = new JTextField(15);
        returnBookIdField = new JTextField(15);
        returnDateField = new JTextField(15);
        
        // Set today's date as default
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        returnDateField.setText(dateFormat.format(new Date()));
        
        // Form layout
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(returnMemberIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(returnBookIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Return Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(returnDateField, gbc);
        
        // Buttons
        JButton returnButton = new JButton("✅ Return Book");
        JButton clearButton = new JButton("🗑️ Clear Form");
        
        returnButton.addActionListener(new ReturnBookActionListener());
        clearButton.addActionListener(e -> clearReturnForm());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(returnButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);
        
        // Borrowed books table
        String[] columnNames = {"Member ID", "Book ID", "Title", "Borrow Date", "Due Date", "Days Overdue"};
        borrowTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        borrowTable = new JTable(borrowTableModel);
        borrowTable.setRowHeight(25);
        borrowTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(borrowTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Currently Borrowed Books"));
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        // Load borrowed books data
        updateBorrowTable();
        
        // Arrange components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createFinePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("💰 Fine Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Calculate Fines"));
        
        fineMemberIdField = new JTextField(15);
        JButton calculateButton = new JButton("🧮 Calculate Fines");
        JButton clearButton = new JButton("🗑️ Clear");
        
        calculateButton.addActionListener(e -> calculateMemberFines());
        clearButton.addActionListener(e -> {
            fineMemberIdField.setText("");
            fineResultArea.setText("");
        });
        
        inputPanel.add(new JLabel("Member ID:"));
        inputPanel.add(fineMemberIdField);
        inputPanel.add(calculateButton);
        inputPanel.add(clearButton);
        
        // Result area
        fineResultArea = new JTextArea(20, 60);
        fineResultArea.setEditable(false);
        fineResultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        fineResultArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(fineResultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Fine Calculation Results"));
        
        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Fine Policy"));
        JLabel infoLabel = new JLabel(
            "<html><b>Fine Policy:</b> $5.00 per day for overdue books<br>" +
            "<b>Grace Period:</b> None - fines start from the day after due date</html>"
        );
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        // Arrange components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(infoPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    // Method implementations
    private void performBookSearch() {
        String searchTerm = bookSearchField.getText().trim().toLowerCase();
        updateBookTable(searchTerm);
    }
    
    private void updateBookTable(String searchTerm) {
        bookTableModel.setRowCount(0);
        
        for (Book book : books) {
            boolean matches = searchTerm.isEmpty() || 
                            book.getTitle().toLowerCase().contains(searchTerm) ||
                            book.getAuthor().toLowerCase().contains(searchTerm) ||
                            book.getIsbn().toLowerCase().contains(searchTerm);
            
            if (matches) {
                Object[] rowData = {
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.isAvailable() ? "✅ Available" : "❌ Borrowed"
                };
                bookTableModel.addRow(rowData);
            }
        }
    }
    
    private void updateBorrowTable() {
        borrowTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        
        for (BorrowRecord record : borrowRecords) {
            try {
                Date dueDate = dateFormat.parse(record.getDueDate());
                long timeDifference = currentDate.getTime() - dueDate.getTime();
                long daysOverdue = Math.max(0, timeDifference / (1000 * 60 * 60 * 24));
                
                Object[] rowData = {
                    record.getMemberId(),
                    record.getBookId(),
                    record.getBookTitle(),
                    record.getBorrowDate(),
                    record.getDueDate(),
                    daysOverdue > 0 ? daysOverdue + " days" : "Not overdue"
                };
                borrowTableModel.addRow(rowData);
            } catch (ParseException e) {
                System.err.println("Error parsing date: " + e.getMessage());
            }
        }
    }
    
    private void calculateMemberFines() {
        String memberId = fineMemberIdField.getText().trim();
        
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a Member ID", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<BorrowRecord> memberRecords = new ArrayList<>();
        for (BorrowRecord record : borrowRecords) {
            if (record.getMemberId().equalsIgnoreCase(memberId)) {
                memberRecords.add(record);
            }
        }
        
        if (memberRecords.isEmpty()) {
            fineResultArea.setText("No borrowed books found for Member ID: " + memberId);
            return;
        }
        
        StringBuilder result = new StringBuilder();
        result.append("FINE CALCULATION REPORT\n");
        result.append("=".repeat(50)).append("\n");
        result.append("Member ID: ").append(memberId).append("\n");
        result.append("Calculation Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())).append("\n");
        result.append("=".repeat(50)).append("\n\n");
        
        double totalFine = 0.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        
        int bookCount = 1;
        for (BorrowRecord record : memberRecords) {
            try {
                Date dueDate = dateFormat.parse(record.getDueDate());
                long timeDifference = currentDate.getTime() - dueDate.getTime();
                long daysOverdue = Math.max(0, timeDifference / (1000 * 60 * 60 * 24));
                double bookFine = daysOverdue * 5.0; // $5 per day
                totalFine += bookFine;
                
                result.append("Book #").append(bookCount++).append("\n");
                result.append("Title: ").append(record.getBookTitle()).append("\n");
                result.append("Book ID: ").append(record.getBookId()).append("\n");
                result.append("Borrow Date: ").append(record.getBorrowDate()).append("\n");
                result.append("Due Date: ").append(record.getDueDate()).append("\n");
                result.append("Days Overdue: ").append(daysOverdue).append("\n");
                result.append("Fine Amount: $").append(String.format("%.2f", bookFine)).append("\n");
                result.append("-".repeat(30)).append("\n");
                
            } catch (ParseException e) {
                result.append("Error calculating fine for: ").append(record.getBookTitle()).append("\n");
                result.append("Reason: Invalid date format\n");
                result.append("-".repeat(30)).append("\n");
            }
        }
        
        result.append("\nSUMMARY\n");
        result.append("=".repeat(20)).append("\n");
        result.append("Total Books: ").append(memberRecords.size()).append("\n");
        result.append("TOTAL FINE: $").append(String.format("%.2f", totalFine)).append("\n");
        
        if (totalFine > 0) {
            result.append("\n⚠️ PAYMENT REQUIRED ⚠️\n");
        } else {
            result.append("\n✅ NO FINES DUE ✅\n");
        }
        
        fineResultArea.setText(result.toString());
    }
    
    private void clearBorrowForm() {
        memberIdField.setText("");
        bookIdField.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        borrowDateField.setText(dateFormat.format(new Date()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        dueDateField.setText(dateFormat.format(calendar.getTime()));
    }
    
    private void clearReturnForm() {
        returnMemberIdField.setText("");
        returnBookIdField.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        returnDateField.setText(dateFormat.format(new Date()));
    }
    
    // Action Listener Classes
    private class BorrowBookActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get form data
            String memberId = memberIdField.getText().trim();
            String bookId = bookIdField.getText().trim();
            String borrowDate = borrowDateField.getText().trim();
            String dueDate = dueDateField.getText().trim();
            
            // Validate input
            if (memberId.isEmpty() || bookId.isEmpty() || borrowDate.isEmpty() || dueDate.isEmpty()) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Find the book
            Book bookToBorrow = null;
            for (Book book : books) {
                if (book.getId().equals(bookId)) {
                    bookToBorrow = book;
                    break;
                }
            }
            
            if (bookToBorrow == null) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "Book with ID '" + bookId + "' not found!", "Book Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!bookToBorrow.isAvailable()) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "Book '" + bookToBorrow.getTitle() + "' is already borrowed!", 
                    "Book Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Process the borrowing
            bookToBorrow.setAvailable(false);
            BorrowRecord newRecord = new BorrowRecord(memberId, bookId, bookToBorrow.getTitle(), borrowDate, dueDate);
            borrowRecords.add(newRecord);
            
            // Show success message
            JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                "Book '" + bookToBorrow.getTitle() + "' successfully borrowed to Member " + memberId + "!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form and update displays
            clearBorrowForm();
            updateBookTable("");
            updateBorrowTable();
        }
    }
    
    private class ReturnBookActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get form data
            String memberId = returnMemberIdField.getText().trim();
            String bookId = returnBookIdField.getText().trim();
            String returnDate = returnDateField.getText().trim();
            
            // Validate input
            if (memberId.isEmpty() || bookId.isEmpty() || returnDate.isEmpty()) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Find the borrow record
            BorrowRecord recordToReturn = null;
            for (BorrowRecord record : borrowRecords) {
                if (record.getMemberId().equalsIgnoreCase(memberId) && 
                    record.getBookId().equals(bookId)) {
                    recordToReturn = record;
                    break;
                }
            }
            
            if (recordToReturn == null) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "No borrowed book found for Member ID '" + memberId + "' and Book ID '" + bookId + "'!", 
                    "Record Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Calculate fine if any
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dueDate = dateFormat.parse(recordToReturn.getDueDate());
                Date returnDateObj = dateFormat.parse(returnDate);
                
                long timeDifference = returnDateObj.getTime() - dueDate.getTime();
                long daysOverdue = Math.max(0, timeDifference / (1000 * 60 * 60 * 24));
                double fine = daysOverdue * 5.0;
                
                String message;
                if (daysOverdue > 0) {
                    message = String.format(
                        "Book '%s' returned successfully!\n\n" +
                        "⚠️ OVERDUE NOTICE:\n" +
                        "Days Overdue: %d\n" +
                        "Fine Amount: $%.2f\n\n" +
                        "Please pay the fine at the library counter.",
                        recordToReturn.getBookTitle(), daysOverdue, fine
                    );
                    JOptionPane.showMessageDialog(LibraryManagementGUI.this, message, 
                        "Book Returned - Fine Due", JOptionPane.WARNING_MESSAGE);
                } else {
                    message = String.format("Book '%s' returned successfully!\n\nNo fines due. Thank you!", 
                        recordToReturn.getBookTitle());
                    JOptionPane.showMessageDialog(LibraryManagementGUI.this, message, 
                        "Book Returned Successfully", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(LibraryManagementGUI.this, 
                    "Invalid date format! Please use YYYY-MM-DD format.", 
                    "Date Format Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Process the return
            for (Book book : books) {
                if (book.getId().equals(bookId)) {
                    book.setAvailable(true);
                    break;
                }
            }
            
            borrowRecords.remove(recordToReturn);
            
            // Clear form and update displays
            clearReturnForm();
            updateBookTable("");
            updateBorrowTable();
        }
    }
    
    // Main method to run the application
    // Main method to run the application - CORRECTED VERSION
public static void main(String[] args) {
    // Set system look and feel - FIXED METHOD NAME
    try {
        // CORRECT METHOD: getSystemLookAndFeelClassName() - NOT getSystemLookAndFeel()
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | 
             IllegalAccessException | UnsupportedLookAndFeelException e) {
        System.err.println("Could not set system look and feel: " + e.getMessage());
        // Fallback to default look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception fallbackException) {
            System.err.println("Could not set fallback look and feel: " + fallbackException.getMessage());
        }
    }
    
    // Create and show GUI on Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
        new LibraryManagementGUI();
    });
}

}
