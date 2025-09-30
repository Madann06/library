public class BorrowRecord {
    private String memberId;
    private String bookId;
    private String bookTitle;
    private String borrowDate;
    private String dueDate;
    
    public BorrowRecord(String memberId, String bookId, String bookTitle, String borrowDate, String dueDate) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public String getMemberId() { 
        return memberId; 
    }
    
    public void setMemberId(String memberId) { 
        this.memberId = memberId; 
    }
    
    public String getBookId() { 
        return bookId; 
    }
    
    public void setBookId(String bookId) { 
        this.bookId = bookId; 
    }
    
    public String getBookTitle() { 
        return bookTitle; 
    }
    
    public void setBookTitle(String bookTitle) { 
        this.bookTitle = bookTitle; 
    }
    
    public String getBorrowDate() { 
        return borrowDate; 
    }
    
    public void setBorrowDate(String borrowDate) { 
        this.borrowDate = borrowDate; 
    }
    
    public String getDueDate() { 
        return dueDate; 
    }
    
    public void setDueDate(String dueDate) { 
        this.dueDate = dueDate; 
    }
    
    @Override
    public String toString() {
        return String.format("BorrowRecord{memberId='%s', bookId='%s', bookTitle='%s', borrowDate='%s', dueDate='%s'}", 
                           memberId, bookId, bookTitle, borrowDate, dueDate);
    }
}

    

