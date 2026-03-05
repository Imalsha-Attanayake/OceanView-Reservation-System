package com.oceanview.client;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OceanViewApp extends JFrame {

    private static final String BASE_URL = "http://localhost:8080/api";

    // Login UI
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblLoginStatus;

    // Main UI
    private JPanel mainPanel;

    // Styled output (green/red)
    private JTextPane outputPane;
    private StyledDocument outputDoc;
    private Style styleNormal, styleSuccess, styleError, styleHeader;

    // Reservation fields
    private JTextField txtGuestName, txtContact, txtRoomId, txtCheckIn, txtCheckOut, txtTotalBill, txtResId;

    // Placeholders
    private HintTextFieldHint hintGuestName;
    private HintTextFieldHint hintContact;
    private HintTextFieldHint hintRoomId;
    private HintTextFieldHint hintCheckIn;
    private HintTextFieldHint hintCheckOut;
    private HintTextFieldHint hintTotalBill;
    private HintTextFieldHint hintResId;

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    public OceanViewApp() {
        setTitle("Ocean View Reservation System");
        setSize(980, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new CardLayout());

        add(buildLoginPanel(), "LOGIN");
        add(buildMainPanel(), "MAIN");

        showCard("LOGIN");
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Ocean View Reservation System - Login");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Login");
        lblLoginStatus = new JLabel(" ");

        btnLogin.setPreferredSize(new Dimension(160, 38));

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        panel.add(title, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Username:"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        panel.add(txtUsername, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(new JLabel("Password:"), gc);

        gc.gridx = 1;
        gc.gridy = 2;
        panel.add(txtPassword, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        panel.add(btnLogin, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 2;
        panel.add(lblLoginStatus, gc);

        btnLogin.addActionListener(e -> doLogin());

        return panel;
    }

    private JPanel buildMainPanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ===== Left menu buttons =====
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        Dimension btnSize = new Dimension(180, 42);

        JButton btnViewRooms = makeMenuButton("View Rooms", btnSize);
        JButton btnAddRes = makeMenuButton("Add Reservation", btnSize);
        JButton btnViewRes = makeMenuButton("View Reservation", btnSize);
        JButton btnCalculateBill = makeMenuButton("Calculate Bill", btnSize);
        JButton btnUpdateRes = makeMenuButton("Update Reservation", btnSize);
        JButton btnDeleteRes = makeMenuButton("Delete Reservation", btnSize);
        JButton btnHelp = makeMenuButton("Help", btnSize);
        JButton btnLogout = makeMenuButton("Logout", btnSize);

        left.add(btnViewRooms);
        left.add(Box.createVerticalStrut(10));
        left.add(btnAddRes);
        left.add(Box.createVerticalStrut(10));
        left.add(btnViewRes);
        left.add(Box.createVerticalStrut(10));
        left.add(btnCalculateBill);
        left.add(Box.createVerticalStrut(10));
        left.add(btnUpdateRes);
        left.add(Box.createVerticalStrut(10));
        left.add(btnDeleteRes);
        left.add(Box.createVerticalStrut(10));
        left.add(btnHelp);
        left.add(Box.createVerticalStrut(10));
        left.add(btnLogout);

        // ===== Output as JTextPane (colored) =====
        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputPane.setMargin(new Insets(10, 10, 10, 10));

        outputDoc = outputPane.getStyledDocument();
        initStyles();

        JScrollPane scroll = new JScrollPane(outputPane);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // ===== Bottom form (highlighted) =====
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 248, 255));

        TitledBorder tb = BorderFactory.createTitledBorder("Reservation Inputs");
        tb.setTitleFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.setBorder(new CompoundBorder(tb, new EmptyBorder(10, 10, 10, 10)));

        JPanel formGrid = new JPanel(new GridLayout(2, 6, 10, 8));
        formGrid.setOpaque(false);

        Font labelFont = new Font("Arial", Font.BOLD, 12);

        txtResId = new JTextField();
        txtGuestName = new JTextField();
        txtContact = new JTextField();
        txtRoomId = new JTextField();
        txtCheckIn = new JTextField();
        txtCheckOut = new JTextField();
        txtTotalBill = new JTextField();

        setFieldHeight(txtResId);
        setFieldHeight(txtGuestName);
        setFieldHeight(txtContact);
        setFieldHeight(txtRoomId);
        setFieldHeight(txtCheckIn);
        setFieldHeight(txtCheckOut);
        setFieldHeight(txtTotalBill);

        // Placeholders
        hintResId = new HintTextFieldHint(txtResId, "e.g., 1");
        hintGuestName = new HintTextFieldHint(txtGuestName, "e.g., John Silva");
        hintContact = new HintTextFieldHint(txtContact, "e.g., 0771234567");
        hintRoomId = new HintTextFieldHint(txtRoomId, "e.g., 1");
        hintCheckIn = new HintTextFieldHint(txtCheckIn, "YYYY-MM-DD");
        hintCheckOut = new HintTextFieldHint(txtCheckOut, "YYYY-MM-DD");
        hintTotalBill = new HintTextFieldHint(txtTotalBill, "auto or type amount");

        formGrid.add(makeLabel("Res ID (View/Upd/Del):", labelFont));
        formGrid.add(makeLabel("Guest Name:", labelFont));
        formGrid.add(makeLabel("Contact No:", labelFont));
        formGrid.add(makeLabel("Room ID:", labelFont));
        formGrid.add(makeLabel("Check-in:", labelFont));
        formGrid.add(makeLabel("Check-out:", labelFont));

        formGrid.add(txtResId);
        formGrid.add(txtGuestName);
        formGrid.add(txtContact);
        formGrid.add(txtRoomId);
        formGrid.add(txtCheckIn);
        formGrid.add(txtCheckOut);

        JPanel billPanel = new JPanel(new BorderLayout(10, 0));
        billPanel.setOpaque(false);

        JLabel billLabel = new JLabel("Total Bill:");
        billLabel.setFont(labelFont);

        billPanel.add(billLabel, BorderLayout.WEST);
        billPanel.add(txtTotalBill, BorderLayout.CENTER);

        bottomPanel.add(formGrid, BorderLayout.CENTER);
        bottomPanel.add(billPanel, BorderLayout.SOUTH);

        // ===== Layout =====
        mainPanel.add(left, BorderLayout.WEST);
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ===== Button actions =====
        btnViewRooms.addActionListener(e -> safeRun(() -> {
            String res = sendRequest("GET", "/rooms", null);
            setOutput(formatRooms(res), false);
        }));

        btnAddRes.addActionListener(e -> safeRun(() -> {
            String json = buildReservationJson();
            String res = sendRequest("POST", "/reservations", json);

            boolean success = res.contains("HTTP 200") && res.contains("Reservation created successfully");
            setOutput(res, success);

            // ✅ Auto-clear after successful add
            if (success) {
                clearReservationInputsAfterAdd();
            }
        }));

        btnViewRes.addActionListener(e -> safeRun(() -> {
            String id = hintResId.getRealText();
            if (id.isEmpty()) throw new IllegalArgumentException("Please enter Reservation ID (number).");
            String res = sendRequest("GET", "/reservations/" + id, null);
            setOutput(res, res.contains("HTTP 200"));
        }));

        btnCalculateBill.addActionListener(e -> safeRun(() -> {
            String roomId = txtRoomId.getText().trim();
            String checkIn = txtCheckIn.getText().trim();
            String checkOut = txtCheckOut.getText().trim();

            if (roomId.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                throw new IllegalArgumentException("Enter Room ID, Check-in date and Check-out date to calculate bill.");
            }

            String endpoint = "/reservations/calculate?roomId=" + roomId
                    + "&checkIn=" + checkIn
                    + "&checkOut=" + checkOut;

            String res = sendRequest("GET", endpoint, null);
            setOutput(res, res.contains("HTTP 200"));

            int idx = res.indexOf("\"totalBill\":");
            if (idx != -1) {
                int start = idx + "\"totalBill\":".length();
                int end = res.indexOf("}", start);
                if (end == -1) end = res.length();
                String value = res.substring(start, end).replaceAll("[^0-9.]", "").trim();
                if (!value.isEmpty()) txtTotalBill.setText(value);
            }
        }));

        btnUpdateRes.addActionListener(e -> safeRun(() -> {
            String id = txtResId.getText().trim();
            if (id.isEmpty()) throw new IllegalArgumentException("Please enter Reservation ID to update.");
            String json = buildReservationJson();
            String res = sendRequest("PUT", "/reservations/" + id, json);
            setOutput(res, res.contains("HTTP 200") && res.contains("updated successfully"));
        }));

        btnDeleteRes.addActionListener(e -> safeRun(() -> {
            String id = txtResId.getText().trim();
            if (id.isEmpty()) throw new IllegalArgumentException("Please enter Reservation ID to delete.");
            String res = sendRequest("DELETE", "/reservations/" + id, null);
            setOutput(res, res.contains("HTTP 200") && res.contains("deleted successfully"));
        }));

        btnHelp.addActionListener(e -> setOutput(getHelpText(), false));

        btnLogout.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            lblLoginStatus.setText(" ");
            showCard("LOGIN");
        });

        return mainPanel;
    }

    // ===== Output color system =====
    private void initStyles() {
        styleNormal = outputPane.addStyle("normal", null);
        StyleConstants.setForeground(styleNormal, Color.DARK_GRAY);

        styleSuccess = outputPane.addStyle("success", null);
        StyleConstants.setForeground(styleSuccess, new Color(0, 128, 0));

        styleError = outputPane.addStyle("error", null);
        StyleConstants.setForeground(styleError, new Color(180, 0, 0));

        styleHeader = outputPane.addStyle("header", null);
        StyleConstants.setBold(styleHeader, true);
        StyleConstants.setForeground(styleHeader, Color.BLACK);
    }

    private void setOutput(String text, boolean success) {
        try {
            outputDoc.remove(0, outputDoc.getLength());
            outputDoc.insertString(outputDoc.getLength(), success ? "SUCCESS:\n" : "OUTPUT:\n", styleHeader);
            outputDoc.insertString(outputDoc.getLength(), text + "\n", success ? styleSuccess : styleNormal);
            outputPane.setCaretPosition(0);
        } catch (Exception ignored) {
        }
    }

    private void appendError(String text) {
        try {
            outputDoc.insertString(outputDoc.getLength(), "ERROR: " + text + "\n", styleError);
            outputPane.setCaretPosition(outputDoc.getLength());
        } catch (Exception ignored) {
        }
    }

    // ===== UI helpers =====
    private JButton makeMenuButton(String text, Dimension size) {
        JButton b = new JButton(text);
        b.setPreferredSize(size);
        b.setMaximumSize(size);
        b.setMinimumSize(size);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setFocusPainted(false);
        return b;
    }

    private JLabel makeLabel(String text, Font f) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        return l;
    }

    private void setFieldHeight(JTextField tf) {
        Dimension d = tf.getPreferredSize();
        tf.setPreferredSize(new Dimension(d.width, 30));
    }

    private void clearReservationInputsAfterAdd() {
        txtResId.setText("");
        txtGuestName.setText("");
        txtContact.setText("");
        txtRoomId.setText("");
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        txtTotalBill.setText("");

        // Re-show placeholders
        hintResId.showHintIfEmpty();
        hintGuestName.showHintIfEmpty();
        hintContact.showHintIfEmpty();
        hintRoomId.showHintIfEmpty();
        hintCheckIn.showHintIfEmpty();
        hintCheckOut.showHintIfEmpty();
        hintTotalBill.showHintIfEmpty();
    }

    // ===== Login =====
    private void doLogin() {
        safeRun(() -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblLoginStatus.setText("Please enter username and password.");
                return;
            }

            String json = "{ \"username\":\"" + username + "\", \"password\":\"" + password + "\" }";
            String res = sendRequest("POST", "/login", json);

            if (res.contains("Login successful")) {
                lblLoginStatus.setText("Login successful!");
                showCard("MAIN");
                setOutput("Welcome, " + username + "!\nUse the buttons on the left.", true);
            } else {
                lblLoginStatus.setText("Invalid username/password.");
                setOutput(res, false);
            }
        });
    }

    // ===== Build JSON =====
    private String buildReservationJson() {
        String guestName = txtGuestName.getText().trim();
        String contact = txtContact.getText().trim();
        String roomId = txtRoomId.getText().trim();
        String checkIn = txtCheckIn.getText().trim();
        String checkOut = txtCheckOut.getText().trim();
        String totalBill = txtTotalBill.getText().trim();

        if (guestName.isEmpty() || roomId.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty() || totalBill.isEmpty()) {
            throw new IllegalArgumentException("Please fill Guest Name, Room ID, Check-in, Check-out and Total Bill.");
        }

        return "{"
                + "\"guestName\":\"" + guestName + "\","
                + "\"contactNumber\":\"" + contact + "\","
                + "\"roomId\":" + roomId + ","
                + "\"checkInDate\":\"" + checkIn + "\","
                + "\"checkOutDate\":\"" + checkOut + "\","
                + "\"totalBill\":" + totalBill
                + "}";
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
    }

    private void safeRun(ThrowingRunnable r) {
        try {
            r.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            appendError(ex.getMessage() == null ? ex.toString() : ex.getMessage());
        }
    }

    private String sendRequest(String method, String endpoint, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (jsonBody != null && (method.equals("POST") || method.equals("PUT"))) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
                os.flush();
            }
        }

        int status = conn.getResponseCode();

        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line);

        conn.disconnect();
        return "HTTP " + status + " -> " + response;
    }

    // ===== Rooms formatting =====
    private String formatRooms(String httpResponse) {
        String json = httpResponse;
        int arrowIndex = json.indexOf("->");
        if (arrowIndex != -1) {
            json = json.substring(arrowIndex + 2).trim();
        }

        if (!json.startsWith("[")) {
            return httpResponse;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID   RoomNo   Type        Price       Available\n");
        sb.append("------------------------------------------------\n");

        String cleaned = json.replace("},{", "}\n{");
        String[] objects = cleaned.replace("[", "").replace("]", "").split("\n");

        for (String obj : objects) {
            String id = extractValue(obj, "roomId");
            String roomNo = extractValue(obj, "roomNumber");
            String type = extractValue(obj, "roomType");
            String price = extractValue(obj, "price");
            String avail = extractValue(obj, "availability");

            sb.append(pad(id, 5))
                    .append(pad(roomNo, 9))
                    .append(pad(type, 12))
                    .append(pad(price, 12))
                    .append(avail)
                    .append("\n");
        }

        return sb.toString();
    }

    private String extractValue(String jsonObj, String key) {
        int idx = jsonObj.indexOf("\"" + key + "\":");
        if (idx == -1) return "";

        String after = jsonObj.substring(idx + key.length() + 3).trim();

        if (after.startsWith("\"")) {
            after = after.substring(1);
            int end = after.indexOf("\"");
            return end == -1 ? after : after.substring(0, end);
        } else {
            int endComma = after.indexOf(",");
            int endBrace = after.indexOf("}");
            int end = (endComma == -1) ? endBrace : Math.min(endComma, endBrace);
            if (end == -1) end = after.length();
            return after.substring(0, end).trim();
        }
    }

    private String pad(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        return text + " ".repeat(width - text.length());
    }

    private String getHelpText() {
        return "HELP - Ocean View Reservation System\n"
                + "----------------------------------\n"
                + "1) Login using admin credentials.\n"
                + "2) View Rooms: shows room list.\n"
                + "3) Calculate Bill: enter roomId + dates, click Calculate Bill (fills Total Bill).\n"
                + "4) Add Reservation: fill details and click Add (auto clears after success).\n"
                + "5) View/Update/Delete using Reservation ID.\n";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OceanViewApp().setVisible(true));
    }

    // =========================================================
    // Placeholder helper (no extra libraries)
    // =========================================================
    private static class HintTextFieldHint {
        private final JTextField field;
        private final String hint;
        private final Color normalColor;
        private final Color hintColor = Color.GRAY;

        HintTextFieldHint(JTextField field, String hint) {
            this.field = field;
            this.hint = hint;
            this.normalColor = field.getForeground();
            showHintIfEmpty();

            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (isShowingHint()) {
                        field.setText("");
                        field.setForeground(normalColor);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    showHintIfEmpty();
                }
            });
        }

        boolean isShowingHint() {
            return field.getForeground().equals(hintColor) && field.getText().equals(hint);
        }

        String getRealText() {
            return isShowingHint() ? "" : field.getText().trim();
        }

        void showHintIfEmpty() {
            if (field.getText().trim().isEmpty()) {
                field.setText(hint);
                field.setForeground(hintColor);
            }
        }
    }
}