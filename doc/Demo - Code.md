# **Live Coding: New User Story Implementation (60 Min)** 🚀

**Format**: Hands-on Workshop with Real Code

**Scenario**: Product Owner provides: "As a user, I want to view a beschreibung for each Produkt."

## **Complete BDD/ATDD Workflow Demonstration** 🎯

### **Phase A: First overview (15 min)**

- **Hack 0**: As any LLM how to ask claude to ask about the project ;)
- **Prompt 1 (1 min + 2 min review)**:

```
Analyze the provided codebase. Give me a high-level overview of this project. Include:
1.  The primary purpose of the software.
2.  The main programming language(s) and version(s).
3.  The key frameworks and major libraries used (e.g., React, Django, Spring Boot, Express.js).
4.  The package manager and dependency file (e.g., package.json, requirements.txt, pom.xml).
```

- **Quick Feedback**: Für wen war das jetzt neu?
- **Prompt 2 (1 min + 2 min review)**:

```
Based on the project files (especially the README, package files, and any Dockerfiles or setup scripts), provide a step-by-step guide to set up the local development environment. Include:
1.  System prerequisites (e.g., Node.js v18, Python 3.10, Java 17).
2.  How to install dependencies.
3.  How to configure required environment variables (explain what a `.env` file might need).
4.  Instructions for setting up necessary services, like a database.
5.  The command to start the application locally.
6.  The command to run the test suite.
```

- **Quick Feedback**: Für wen war das jetzt neu?
- **Prompt 3 (3 min + n min review)**:

```
Analyze the directory structure and key files. Describe the software architecture.
1.  What architectural pattern does it seem to follow (e.g., MVC, Microservices, Layered Architecture)?
2.  Explain the purpose of the main directories (e.g., 'src', 'app', 'components', 'controllers', 'services').
3.  Identify the main entry point file(s) for the application and the primary configuration files.
```

- **Hack 1**:

```
Can you somehow remmeber that so that we do not need to do another analysis the next time I am asking you these questions?
```

```
Did you remember the ansers to all three prompts or only to the last one?
```

- **Hack 2**: You can actually use SuperClaude for that. `/load`
- **Fun 0**: Lets look at the costs.

### **Phase B: Run the application and look at the data (15 min)**

- **Prompt 1 (2 min + 1 min review)**:

```
I want you to run the application now so I can call the procuts api. Can you tell me the endpoint and run the application for me?
```

- **Prompt 2 (TODO min + TODO min review)**:

```
The application was running but there is no data to be shown. Can you add some data to the database so I can see actual products?
```

```
There are still to products shown when calling the api.
```

```
It seems that the liefertag is missing from the data and thats why we cannot load it.
```

```
Looking back at the task of adding the data into the database, how can you prevent that you are going to make the same mistakes again?
```

```
/compact or /clear
```

### **Phase C: Build the new feature based on prd (30 min)**

```
I want you to implement a feature for me now. Start by using /Users/bstemmildt/dev/hackersandwizards/demo/.taskmaster/beschreibung_prd.md to create a comprehensive prd based on this file. Use taskmaster to create this prd. Get documentation with context7.
```

```
Please analyze the complexity of each task now.
```

```
Please create all subtasks now.
```

```
I want you to implement the current prd based on all tasks of taskmaster and EXACTLY following our TDD development process from CLAUDE.md. Use taskmaster-cli.
```
