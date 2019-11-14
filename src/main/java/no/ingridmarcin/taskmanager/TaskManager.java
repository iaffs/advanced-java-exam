package no.ingridmarcin.taskmanager;


import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;
import org.flywaydb.core.Flyway;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class TaskManager {

    public static void main(String[] args) throws IOException, SQLException {
        Scanner input = new Scanner(System.in);

        mainMenuWindow(input);
    }

    // MAIN METHODS

    private static void mainMenuWindow(Scanner input) throws IOException, SQLException {
        System.out.println("**********\n" +
                "Main Menu \n" +
                "**********\n"
                + "1. Members \n"
                + "2. Projects \n"
                + "3. Assign member to a project \n"
                + "4. Exit program");
        int userChoice = Integer.parseInt(input.nextLine());

        if (userChoice == 1) {
            addNewMemberInterface(input);
        } else if (userChoice == 2) {
            addNewProjectInterface(input);
        } else if (userChoice == 3) {
            assignMemberToProjectInterface(input);
        } else if (userChoice == 4) {
            System.exit(0);
        }
    }

    private static void assignMemberToProjectInterface(Scanner input) throws IOException, SQLException {
        PGSimpleDataSource dataSource = getDataSource();
        ProjectDao projectDao = new ProjectDao(dataSource);
        MemberToProjectDao memberToProjectDao = new MemberToProjectDao(dataSource);
        MemberToProject memberToProject = new MemberToProject();
        MemberDao memberDao = new MemberDao(dataSource);

        System.out.println(
                "******************************\n" +
                        "Assign member to the project  \n" +
                        "******************************\n" +
                        "1. Assign \n" +
                        "2. Back to main menu \n");
        int userChoice = Integer.parseInt(input.nextLine());

        if (userChoice == 1) {
            assignNewProject(input, projectDao, memberToProjectDao, memberToProject, memberDao);
        } else if (userChoice == 2) {
            mainMenuWindow(input);
        }
    }


    private static void addNewProjectInterface(Scanner input) throws IOException, SQLException {
        PGSimpleDataSource dataSource = getDataSource();
        ProjectDao projectDao = new ProjectDao(dataSource);
        System.out.println(
                "********\n" +
                        "Projects  \n" +
                        "********\n");
        printProjectsList(projectDao);

        System.out.println(
                "1. Add a new project\n" +
                        "2. Back to main menu");
        int userChoice = Integer.parseInt(input.nextLine());

        if (userChoice == 1) {
            addNewProject(input, projectDao);
        } else if (userChoice == 2) {
            mainMenuWindow(input);
        }
    }


    private static void addNewMemberInterface(Scanner input) throws IOException, SQLException {
        PGSimpleDataSource dataSource = getDataSource();
        MemberDao memberDao = new MemberDao(dataSource);
        System.out.println(
                "********\n" +
                        "Members  \n" +
                        "********\n");
        printMembersList(memberDao);

        System.out.println(
                "1. Add a new member\n" +
                        "2. Back to main menu");

        int userChoice = Integer.parseInt(input.nextLine());

        if (userChoice == 1) {
            addNewMember(input, memberDao);
        } else if (userChoice == 2) {
            mainMenuWindow(input);
        }
    }


    // METHODS USED IN MAIN METHODS

    private static String checkIfAnyMembersAssigned(MemberDao memberDao, long userChoiceProject) throws SQLException {
        if (memberDao.listAssignedMembers(userChoiceProject).isEmpty()) {
            return "None";
        } else {
            return memberDao.listToString(memberDao.listAssignedMembers(userChoiceProject));
        }
    }

    private static void printProjectsList(ProjectDao projectDao) throws SQLException {
        System.out.println(
                "List of all projects:\n" +
                        " ID |  Name\n" +
                        "--------------\n" +
                        projectDao.listToString(projectDao.listAll()) +
                        "--------------\n");
    }

    private static void printMembersList(MemberDao memberDao) throws SQLException {
        System.out.println(
                "List of all members:\n" +
                        " ID |  Name + Mail\n" +
                        "-------------------------\n" +
                        memberDao.listToString(memberDao.listAll()) +
                        "-------------------------\n");
    }


    private static PGSimpleDataSource getDataSource() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("taskmanager.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        dataSource.setUser(properties.getProperty("dataSource.user"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
    }

    private static void addNewProject(Scanner input, ProjectDao projectDao) throws IOException, SQLException {
        System.out.println("Add a new project name:");
        String projectName = input.nextLine();

        if (projectName.isEmpty()) {
            System.out.println("You didn't write any name. Try again");
            addNewProjectInterface(input);
        } else {
            Project project = new Project();
            project.setProjectName(projectName);
            projectDao.insert(project);
            addNewProjectInterface(input);
        }
    }

    private static void addNewMember(Scanner input, MemberDao memberDao) throws IOException, SQLException {
        System.out.println("Add a new member name:");
        String memberName = input.nextLine();
        System.out.println("Add a new member mail:");
        String memberMail = input.nextLine();

        if (memberName.isEmpty() || memberMail.isEmpty()) {
            System.out.println("You must fill both fields. Try again");
            addNewMemberInterface(input);
        } else {
            Member member = new Member();
            member.setMemberName(memberName);
            member.setMail(memberMail);
            memberDao.insert(member);
            addNewMemberInterface(input);
        }
    }


    private static void assignNewProject(Scanner input, ProjectDao projectDao, MemberToProjectDao memberToProjectDao,
                                         MemberToProject memberToProject, MemberDao memberDao) throws SQLException, IOException {
        printProjectsList(projectDao);
        System.out.println("Choose a project ID:");
        int userChoiceProject = Integer.parseInt(input.nextLine());

        System.out.println("Project : " + projectDao.listToString(projectDao.listSelectedProjects(userChoiceProject)));

        System.out.println("Members already assigned to this project: \n" + checkIfAnyMembersAssigned(memberDao, userChoiceProject) + "\n");
        printMembersList(memberDao);

        System.out.println("Choose ID of a member you want to add to this project:");
        int userChoiceMember = Integer.parseInt(input.nextLine());

        System.out.println(memberDao.listToString(memberDao.listAssignedMembers(userChoiceProject)));

        memberToProject.setProjectId(userChoiceProject);
        memberToProject.setMemberId(userChoiceMember);
        try {
            memberToProjectDao.insertMemberToProject(memberToProject);
            System.out.println("Members assigned to this project:\n"
                    + memberDao.listToString(memberDao.listAssignedMembers(userChoiceProject)));

        } catch (PSQLException e) {
            if (memberToProjectDao.selectUnique(userChoiceProject, userChoiceMember).size() > 0) {
                System.out.println("This member is already assigned to this project");
                assignMemberToProjectInterface(input);
            } else {
                System.out.println("Unhandled exception in function assignMemberToProject \n" + e);
            }
        }
        assignMemberToProjectInterface(input);
    }
}

