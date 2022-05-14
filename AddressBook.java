
    import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;


    @SuppressWarnings("serial")
    public class AddressBook  extends JFrame {
        private static ArrayList<Person> people = new ArrayList<>();
        JPanel inputPanel = new JPanel(), mainPanel = new JPanel(), sortPanel = new JPanel(), searchPanel = new JPanel(),
                editPanel = new JPanel(), currentPanel = mainPanel;
        JLabel labels[] = { new JLabel("Name: "), new JLabel("Phone number: "), new JLabel("Email address: "),
                new JLabel("Street address: "), new JLabel("Date of birth: ") };
        JTextField inputFields[] = new JTextField[5],
                enterName = new JTextField("Please enter the name or telephone number.");
        JButton inputChoise = new JButton("Register new people"), sortChoise = new JButton("Display all people"),
                searchChoise = new JButton("Find people"), inputSubmit = new JButton("Submit"),
                sortReturn = new JButton("Back to main menu"), searchReturn = new JButton("Back to main menu");
        JTextArea searchArea = new JTextArea(), areas = new JTextArea();


        protected void init() {
            setSize(1000, 700);
            setComponent(mainPanel, this, 1000, 700, 0, 0, true);
            setComponent(inputPanel, this, 1000, 700, 0, 0, false);
            setComponent(sortPanel, this, 1000, 700, 0, 0, false);
            setComponent(searchPanel, this, 1000, 700, 0, 0, false);
            setComponent(new JLabel("Welome to the Address Book Application!"), mainPanel, 300, 20, 100, 20, true);
            setComponent(new JLabel("What would you like to do?"), mainPanel, 200, 20, 150, 50, true);
            setComponent(inputChoise, mainPanel, 200, 40, 100, 100, true);
            setComponent(sortChoise, mainPanel, 200, 40, 100, 170, true);
            setComponent(searchChoise, mainPanel, 200, 40, 100, 240, true);
            setComponent(sortReturn, sortPanel, 200, 40, 400, 10, true);
            setComponent(searchReturn, searchPanel, 200, 40, 700, 10, true);
            setComponent(searchArea, searchPanel, 200, 400, 350, 100, true);
            sortReturn.addActionListener(event -> displayPanel(mainPanel));
            searchReturn.addActionListener(event -> displayPanel(mainPanel));
            inputChoise.addActionListener(event -> displayPanel(inputPanel));
            searchChoise.addActionListener(event -> displayPanel(searchPanel));
            sortChoise.addActionListener(event -> {
                displayPanel(sortPanel);
                // deserializeAll();
                Collections.sort(people);
                for (int i = 0; i < people.size(); i++) {
                    areas.setText(areas.getText() + "Name: " + people.get(i).getName() + "\n " + "Phone: "
                            + people.get(i).getPhoneNmb() + "\n " + "Email: " + people.get(i).getEmail() + "\n "
                            + "Address: " + people.get(i).getStreetAddress() + "\n " + "BirthDate: "
                            + people.get(i).getBirthDate() + "\n ");
                }
            });
            for (int i = 0, j = 20; i < 5; i++, j += 50) {
                setComponent(labels[i], inputPanel, 100, 20, 50, j, true);
                setComponent(inputFields[i] = new JTextField(), inputPanel, 100, 20, 200, j, true);
            }
            setComponent(areas, sortPanel, 200, 500, 200, 70, true);
            setComponent(inputSubmit, inputPanel, 80, 40, 450, 600, true);
            inputSubmit.addActionListener(event -> {
                people.add(new Person(inputFields[0].getText(), inputFields[1].getText(), inputFields[2].getText(),
                        inputFields[3].getText(), inputFields[4].getText()));
                displayPanel(mainPanel);
            });
            setComponent(enterName, searchPanel, 200, 20, 350, 10, true);
            enterName.addActionListener(event -> {
                searchArea.removeAll();
                String input = enterName.getText();
                JTextArea searchArea = new JTextArea(200, 30);
                boolean byName = input.matches("[A-Za-z]||\\s+");
                for (Person p : people) {
                    if (input.equals(byName ? p.getName() : p.getPhoneNmb()))
                        searchArea.setText(searchArea.getText() + p.getName() + " " + p.getPhoneNmb());
                }
            });
            setVisible(true);
        }


        protected void setComponent(JComponent jc, Component place, int w, int h, int x, int y, boolean v) {
            jc.setSize(w, h);
            jc.setLocation(x, y);
            if (place == this)
                ((JFrame) place).add(jc);
            else
                ((JComponent) place).add(jc);
            jc.setLayout(null);
            jc.setVisible(v);
        }


        public void serializeAll() {
            for (Person p : people)
                p.serialize();
        }


        public void deserializeAll() {
            people.clear();
            Person p = null;
            try (FileInputStream fis = new FileInputStream("personSer.ser");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                while (ois.available() > 0) {
                    p = (Person) ois.readObject();
                    people.add(p);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }


        class Person implements Comparable<Person>, Serializable {
            private String name, phoneNmb, email, streetAddress, birthDate;


            Person(String n, String p, String e, String s, String b) {
                name = n;
                phoneNmb = p;
                email = e;
                streetAddress = s;
                birthDate = b;
                serialize();
            }


            public String getName() {
                return name;
            }


            public void setName(String name) {
                this.name = name;
            }


            public String getPhoneNmb() {
                return phoneNmb;
            }


            public void setPhoneNmb(String phoneNmb) {
                this.phoneNmb = phoneNmb;
            }


            public String getEmail() {
                return email;
            }


            public void setEmail(String email) {
                this.email = email;
            }


            public String getStreetAddress() {
                return streetAddress;
            }


            public void setStreetAddress(String streetAddress) {
                this.streetAddress = streetAddress;
            }


            public String getBirthDate() {
                return birthDate;
            }


            public void setBirthDate(String birthDate) {
                this.birthDate = birthDate;
            }


            @Override
            public int compareTo(Person p) {
                return name.compareTo(p.getName());
            }


            protected void serialize() {
                try (FileOutputStream fs = new FileOutputStream("personSer.ser", true);
                     ObjectOutputStream os = new ObjectOutputStream(fs)) {
                    os.writeObject(this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }


            protected Person deserialize() {
                Person p = null;
                try (FileInputStream fis = new FileInputStream("personSer.ser");
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    p = (Person) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return p;
            }


        }


        protected void displayPanel(JPanel jp) {
            if (jp != currentPanel) {
                currentPanel.setVisible(false);
                currentPanel = jp;
            }
            jp.setVisible(true);
        }


        public static void main(String args[]) {
            new AddressBook().init();
        }


    }


