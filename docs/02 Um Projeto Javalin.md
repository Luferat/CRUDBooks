# Um Projeto Javalin

Vamos usar a metodologia "passo-a-passo" para criar um pequeno projeto didático de exemplo usando o framework _Javalin_.
O projeto será um **cadastro de livros** que vamos chamar de "**CRUDBooks**" com uma API REST simples com JSON e com os seguintes requisitos:

- Nome do projeto: `CRUDBooks`
- IDE: `IntelliJ CE`
- Framework HTTP: `javalin`
- Gestão: `Maven`
- Persistência: `H2 Database`
- Otimizações: `Lombok`, `HikariCP`, `Jackson`, ...

## Criando o Projeto

1. Abra o **IntelliJ CE**
2. Clique em `File → New Project` ou no botão `[New Project]`, dependendo do contexto atual
3. No painel esquerdo, selecione **Maven Archetype**
4. No painel direito:
   - Name: `CRUDBooks`
   - Location: `~\Documents\java`
   - [x] Create Git repository
   - JDK: `temirin-21` ou outro JDK 21
   - Catalog: `Internal`
   - Archetype: `org.apache.maven.archetypes:maven-archetype-quickstart`
   - Version: `1.1`
   - Advanced settings:
      - GroupId: `com.crudbooks`
      - ArtifactId: `api`
5. Clique em `[Create]`

O IntelliJ irá gerar a estrutura básica de um projeto Maven.
Você deverá ver uma janela semelhante a esta no seu IntelliJ:

```
CRUDBook                  ←→ Raiz do projeto
├── .idea/                ←→ Configurações da IDE
├── src/                  ←→ Raiz do aplicativo
│   ├── main/             ←→ Componentes do aplicativo
│   │   └── java/         ←→ Componentes Java
│   └── test/             ←→ Testes unitários
│       └── java/         ←→ Testes unitários Java
├── .gitignore            ←→ Arquivos e pastas ignorados no versionamento
└── pom.xml               ←→ Configuração do projeto
```

Para testar, clique direito em `App` e selecione `Run 'App.main()`.
Deve aparecer algo no terminal como:

```cmd
Olá, CRUDBook!
Process finished with exit code 0
```

Só continue se os procedimentos acima funcionaram! 😉

## Dependências

Vamos adicionar as primeiras dependências ao projeto:

1. Abra o `pom.xml` para edição e crie a tag `<dependencies></dependencies>` logo após `</properties>`
2. Acesse o site [Maven Repository](https://mvnrepository.com/)
3. Na caixa de pesquisa, digite `javalin` e tecle <kbd>Enter</kbd>
4. Clique no primeiro resultado da pesquisa
5. Na listagem, selecione a versão **estável** mais recente
6. Clique no código para copiá-lo para a área de transferência
7. Cole no `pom.xml`, dentro da tag `<dependencies>`, sem alterar o que já tem lá
8. Repita o processo para 'h2', 'jackson-databind', 'slf4j-simple' e 'lombok'

Após a tag `</dependencies>`, adicione a tag abaixo que permite a compilação do aplicativo nos parâmetros corretos:

```xml
  <build>
   <plugins>
      <!-- Plugin de compilação -->
      <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-compiler-plugin</artifactId>
         <version>3.11.0</version>
         <configuration>
            <source>15</source>
            <target>15</target>
         </configuration>
      </plugin>
   </plugins>
</build>
```

No final, seu `pom.xml` deve ser algo como:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>

   <groupId>com.crudbooks</groupId>
   <artifactId>api</artifactId>
   <version>1.0-SNAPSHOT</version>
   <packaging>jar</packaging>

   <name>api</name>
   <url>http://maven.apache.org</url>

   <properties>
      <maven.compiler.source>21</maven.compiler.source>
      <maven.compiler.target>21</maven.compiler.target>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
   </properties>

   <dependencies>
      <!-- Javalin - versão atual conforme Maven Central -->
      <dependency>
         <groupId>io.javalin</groupId>
         <artifactId>javalin</artifactId>
         <version>6.6.0</version>
      </dependency>

      <!-- SLF4J - logging simples -->
      <dependency>
         <groupId>org.slf4j</groupId>
         <artifactId>slf4j-simple</artifactId>
         <version>2.0.13</version>
      </dependency>

      <!-- H2 Database -->
      <dependency>
         <groupId>com.h2database</groupId>
         <artifactId>h2</artifactId>
         <version>2.2.224</version>
      </dependency>

      <dependency>
         <groupId>org.projectlombok</groupId>
         <artifactId>lombok</artifactId>
         <version>1.18.38</version>
         <scope>provided</scope>
      </dependency>

   </dependencies>

   <build>
      <plugins>
         <!-- Plugin de compilação -->
         <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
               <source>15</source>
               <target>15</target>
            </configuration>
         </plugin>
      </plugins>
   </build>
</project>
```

> Conforme o andamento do projeto, outras dependências podem ser adicionadas ao `pom.xml`.

Se tem o "Maven" instalado no sistema, abre um terminal e comande:

```cmd
mvn clean install
```

Todas as dependências serão baixadas, confirmando que o 'pom.xml' está ok.
Se não tem o Maven fora da _sandbox_, clique direito no nome do projeto, selecione "Mavem" depois "Sync Project".

> **IMPORTANTE!** _À partir daqui, todas as classes Java que criamos ficam em _packages_ abaixo de `com.crudlivros.api`. Além disso, mesmo que não mostre no tutorial, todas as classes devem ter a extensão `.java`, por exemplo, `model.Book` é na verdade `src\main\java\com\crudbooks\api\model\Book.java`. Lembre-se disso!_

---
[↑ Topo](#file-01-javalin-md)
