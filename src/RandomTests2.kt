import javax.swing.*

fun main() {
    val(time1, time2)=getTwoInputs().split(",")
    println("$time1 to $time2")
}
private fun getTwoInputs(): String {
    val xField = JTextField(5);
    val yField = JTextField(5);

    val myPanel = JPanel();
    myPanel.add(JLabel("From:"));
    myPanel.add(xField);
    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
    myPanel.add(JLabel("To:"));
    myPanel.add(yField)

    JOptionPane.showConfirmDialog(null, myPanel,
        "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
    return "${xField.text},${yField.text}"}