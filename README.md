# Java-c-compiler

**Olá, Eu criei um compilador!!!**

Compilador feito em linguagem Java para Compilar linguagem C.

Durante o 3º Ano da minha licenciatura tive uma disciplina denominada **Compilador** onde fomos orientado pelo [Dr. André Filemon](https://www.linkedin.com/in/andr%C3%A9-filemon-87863897/?originalSubdomain=ao) como se deve criar um compilador!

Para a criação deste compilador utilizamos seguintes fundamentos:

1.  Expressão Regular
2.  Autómato (Finito Determinístico)
3.  **Análise Léxica ( 1º fase de um compilador )**
4.  **Analise Sintática ( 2º fase de um compilador )**
    1. Gramática Livre de Contexto ( Top-Down )
    2. Técnica de Detenção de Erro ( Modo Pânico )
    3. Tokens de Sincronização
    4. Técnica de derivação ( First - Flow )
5.  **Analise Semântica ( 3º fase de um compilador )**

A linguagem de estudo do mesmo compilador foi a **Linguagem C** e a linguagem de implementação do compilador foi a **Linguagem Java**.

## **1.1 Porque usar C e Java para cria o compilador ?**

Para o meu trabalho eu escolhi utilizar a **Linguagem C** porque é uma linguagem muito utilizada para fins didático e ela possui um numero de estados reduzido e um número de palavras reservadas reduzidos assim é simples aplicar uma ideia inicial e depois escalar para quando se for desenvolver compiladores para outras linguagens orientadas a objecto e de tripagem dinâmica.

O Java é uma linguagem muito utilizada também para fins didático, e durante o 2 e 3 ano da faculdade estudamos a linguagem java para resolução de algoritmos e a sua vertente no paradigma orientado a objecto, como tinha uma forte base de java então decidi utilizar ela como a linguagem de implementação que iria receber o código escrito em C e compilar.

## **1.2 Uso de Expressão Regular & Autómatos**

É sabido por todos nós que uma variável obedece um conjunto de regras para ser declarada de acordo a linguagem de programação em uso, por exemplo no c não podemos ter o nome de variável iniciando com um numero int 3; Então para que as regras de declaração de variável seja cumprida utilizamos um expressão regular contendo as regras para posterior ser validada com este exemplo tem mais exemplos onde fizemos o uso da expressão regular!

1. Váriavel:
   _ID->_ **[A-Za-z]|_{[A-Za-z]|_|[0-9]}^\***
2. Números Inteiros:
   _INT->_ **[0-9]^+**
3. Números Reais:
   _REAL->_ **[0-9]^+.[0-9]^+**

Ai tem alguns exemplos de expressões regulares que usamos, para validar algumas regras mais simples, tem regras complexas que não é possível fazer a representação só com expressão regular então ai onde entrou a Utilização de autómatos, bom existe 3 tipos **#Autómato Finito Determinístico** , **#Autómato Finito Não Determinístico** , **#Autómato Com Movimentos Vazios** .

Para a criação do compilador o autómato a ser utlizado é o **#Autómato Finito Determinístico** Porque ele tem apenas um caminho para cada símbolo lido e ele não possui movimentos vazios! [Ler mais sobre autómatos](https://pt.wikipedia.org/wiki/M%C3%A1quina_de_estados_finitos_n%C3%A3o_determin%C3%ADstica) .

Então esse foi o autómato que eu desenvolvi para o meu caso de estudo na ferramenta [JFLAP](https://www.jflap.org/):

<img alt="TPC - Autómato.jff.png" src="https://github.com/josedomingos919/java-c-compiler/blob/main/docs/TPC%20-%20Aut%C3%B3mato.jff.png?raw=true" data-hpc="true" class="Box-sc-g0xbh4-0 kzRgrI">

O **JFLAP** permite fazer o teste de pequenos cassos de uso e verificar se a expressão está correta ou não.

## **1.3 Análise Léxica ( 1º fase de um compilador )**

A análise léxica também conhecida como **Scanner** é quando o compilador pega o código escrito em um ficheiro de texto faz a leitura, parte cada símbolo e armazena em uma estrutura de dados que pode ser array, list, pilha, fila... e classifica esse símbolo lido ou conjunto de símbolos lidos caso ele reconheça este símbolo. O reconhecimento dos símbolos do compilador é feito com base uma lista de **Tokens** que é previamente criado para classificar cada símbolo lido. [Ver lista de token](https://github.com/josedomingos919/java-c-compiler/blob/main/src/entities/Token.java)

Aqui tem a saída do meu compilador para um analise léxica do código:

```
#include <stdio.h>

int main() {
	int a = 1 + 3;
	/*Hello my compiler*/
	return 0;
}
```

<img alt="lexema.png" src="https://github.com/josedomingos919/java-c-compiler/blob/main/docs/lexema.png?raw=true" data-hpc="true" class="Box-sc-g0xbh4-0 kzRgrI">

## **1.4 Analise Sintática ( 2º fase de um compilador )**

A analise sintática é a parte que segue depois que o compilador consegue ler os símbolos ela é responsável por validar se a escrita de um código está certa ou errada! Ela consegue fazer isso com base uma **Gramática Livre de Contexto** Tal como na língua portuguesa temos uma gramática que dita as regaras também temos uma gramática em cada linguagem de programação que dita as regras de como se deve escrever o código para o meu caso de estudo eu utilizei uma gramática da **linguagem c desorganizada** que na qual eu tive de organizar para poder implementar a primeira fazer da analise sintática. [Ver minha gramática organizada](https://github.com/josedomingos919/java-c-compiler/blob/main/gramatica-organizada.xml)

## **1.4.1 Gramática Livre de Contexto**

As gramáticas da linguagem de programação eles na sua maioria aparecem utilizando uma estrutura **bottom-up** onde a analise do código é feita de baixo para cima e a recursividade é fita da esquerda para a direita. Para o meu compilador utilizamos uma abordagem diferente que é a regra **top-down** de cima para baixo onde é preciso eliminar a recursividade a esquerda se não os ao escrever o código pode ficar em loop infinito.

Nas gramáticas das linguagens de programação um símbolo pode ter vários caminhos para ajudar na representação visual ou na leitura, e muitos geradores de código eles removem essa ambiguidade porque pode gerar loop e eu também tive de resolver essas ambiguidades para poder implementar a primeira fase. [Ver minha gramática organizada](https://github.com/josedomingos919/java-c-compiler/blob/main/gramatica-organizada.xml)

## **1.4.2 Técnica de Detenção de Erro ( Modo Pânico )**

Um compilador ao analisar o código ele não pode para a sua analise ele deve apresentar todos os erros encontrados até ao final do código, mas se no meio da sua Analise o compilador encontrar um erro ele não pode adivinhar o que o programador queria escrever:
**ex.: int soma {}**  
É sabido este código está errado pois depois do soma poderia ser uma abrir parenteses caso estejamos a declarar função ou poderia ser igual para declaramos uma variável com atribuição ou poderia ser ponto e virgula depois dessa instrução soma; no modo pânico o compilador declara erro nessa linha e como ele não pode parar ele vai a busca de um **token de Sincronização ( Reconciliação )** para ele poder continuar a sua Analise esses podem ser ( Palavra Reservada, Tipo de Dados, ; | ( | ), ID etc... ).

## **1.4.3 Técnica de derivação ( First - Flow )**

No processo de detenção de erro é preciso saber se o compilador pode entrar num determinado fluxo ou não, as funções sequem um fluxo, as variáveis um outro fluxo, as expressões e operações aritmética seguem um outro fluxo então o compilador precisa saber se o símbolo lido faz parte das iniciais de qual fluxo para depois mergulhar nele. Quando não aplicado fica muito complicado saber onde mergulhar e pode ocasionar detenção de erros não existentes caso ele entre em um fluxo errado.

Para o código:

```
#include <stdio.h>

int main( {
    int k = 3 int b = 0;
    int a teste = 8;
    return 0;
}
```

<img alt="sintaxi.png" src="https://github.com/josedomingos919/java-c-compiler/blob/main/docs/sintaxi.png?raw=true" data-hpc="true" class="Box-sc-g0xbh4-0 kzRgrI">

## **1.5 Analise Semântica ( 3º fase de um compilador )**

A analise semântica é a fase responsável por verificar se o código escrito obedece a regra semântica da linguagem no caso uma variável do tipo inteiro não pode receber um valor do tipo string, uma função que que recebe um parâmetro do tipo string não pode ser passado um int como argumento, o numero de argumento de de uma função tem que ser garantido na sua chamada. Para a implementação da fase semântica utilizei uma tabela com assinatura de todas as declarações de variável, função e protótipos para no caso de uma ocorrência de uma chamada da função verificar se ela foi declarada ou se pode ser usada nesse escopo.

Ex.:

```
#include <stdio.h>

int main() {
    soma("Teste", 8484);

    int a = 4;
    float b = a;

    return 0;
}

int soma(int a, int b) {
    return a + b;
}
```

saída do analisador semântico:

<img alt="semantic.png" src="https://github.com/josedomingos919/java-c-compiler/blob/main/docs/semantic.png?raw=true" data-hpc="true" class="Box-sc-g0xbh4-0 kzRgrI">

## **1.6 Outras fases**

Existem ainda outras fases para a conclusão do trabalho de compilador que não foram abordadas nesse trabalho **Geração de código intermediário** e **Otimização de código** Não são fazes menos importante mas elas podem ser feitas com ajuda de programas já prontos e existentes no mercado. [Ler mais em](https://pt.wikipedia.org/wiki/Compilador)

## **# Instruções para rodar o projeto**

1.  Baixar o projeto `git clone`
2.  Fazer instalação do Java no VSCode
3.  Ou Fazer a instalação do NetBeans
4.  Abrir o Projecto e Clicar Play, Pode fazer o download do projeto e rodar no **apache netbeans**, sem mistérios.

**OBS:** O código pode ser inserido no ficheiro `input.txt` no diretório raiz

## **# Considerações Finais**

Este foi um trabalho que amei enquanto estudante da cadeira de compiladores no [ISPTEC](https://www.isptec.co.ao/) decidi colocar ele público para que as pessoas que queiram estudar compiladores ou que terão essa cadeira no 3 Ano do Curso de Engenharia Informática, que lhes sirva de apoio para que possam aprender e melhorar muito suas habilidades programação e no entendimento de como funcionam os compiladores!

**Obrigado!**

_by: José Ndonge_
