#Importacion Modulo
import re 

'''
EBNF General
digitos ::= ([0-9])
vocal ::= (AEIOU|aeiou)
vocal_acentuada ::= (ÁÉÍÓÚ|áéíóú)
letras ::= ([A-Z|a-z|Ññ])
string ::= (<vocal>|<vocal_acentuada>|<letras>)
caracteres_permitidos ::= (’¿’|’ ?’|’¡’|’ !’|’,’|’.’|’;’| ’ |’-’|’(’|’)’|’:’| ")
palabra ::= (<string>|<caracteres_permitidos>|<digitos>)
'''

digitos = r"[0-9]"
vocales = r"[AEIOUaeiouÁÉÍÓÚáéíóú]"
letras = r"[A-Za-zÑñ]"
caracteres_permitidos = r"[¿?¡!.,;\-():\" \s]"

STRING = rf"(?:{vocales}|{letras})+"
PALABRA = rf"(?:{STRING}|{caracteres_permitidos}|{digitos})"

"""
***
Parametro 1 : String (str)
***
Tipo de Retorno: String (str)
***
Toma una palabra, la convierte en minúsculas. Si la vocal tiene tilde la intercambia por su versión
sin tilde. Además, limpia cualquier carácter de la palabra.
Retorna la palabra "limpia" para poder hacer las comparaciones.
"""
def CleanPalabra(palabra):
    palabra = palabra.lower()
    #Reemplazar tildes
    palabra = re.sub(r"[á]", "a", palabra)
    palabra = re.sub(r"[é]", "e", palabra)
    palabra = re.sub(r"[í]", "i", palabra)
    palabra = re.sub(r"[ó]", "o", palabra)
    palabra = re.sub(r"[ú]", "u", palabra)
    #Reemplazar lo que no sea letra 
    palabra = re.sub(r"[^A-Za-zÑñ]", "", palabra)
    return palabra

"""
***
Parametro 1 : String (str)
***
Tipo de Retorno: Tupla
***
Lee el archivo de texto estrofas.txt. Identifica las palabras bonus en la primera línea
si es que existen y luego se verifica la validez de las estrofas.
Retorna una tupla con la lista de palabras bonus y una lista de estrofas, donde esta 
última también corresponde a una tupla (versos, validez).
"""
def IdentificarEstrofas(nombre_archivo):
    # Abrir el archivo en modo lectura
    EstrofasArchivo = open(nombre_archivo, "r", encoding="utf-8")
    ContenidoArchivo = EstrofasArchivo.read()
    
    Estrofas= ContenidoArchivo.strip().split("\n")
    # Se identifican palabras bonus fresco, mangos, melón, cielo. Linea 1
    if ("," in Estrofas[0] and Estrofas[1].strip()== "") or Estrofas[1].strip()== "":
        PalabrasBonus= []
        for pbonus in Estrofas[0].split(","):
            pbonus_limpia = CleanPalabra(pbonus)
            PalabrasBonus.append(pbonus_limpia)
        i=2
    else:
        PalabrasBonus = "No hay"
        i=0
    ListaEstrofas = []
    
    while i<len(Estrofas):
        Verso=[]
        ValidezEstrofa= True

        if Estrofas[i] == "":
            i+=1
        while i < len(Estrofas) and Estrofas[i] != "":
            for caracter in Estrofas[i]:
                if not re.match(PALABRA, caracter):
                    ValidezEstrofa= False
            Verso.append(Estrofas[i])
            i+=1
        ListaEstrofas.append((Verso, ValidezEstrofa))
    # Cerrar el archivo 
    EstrofasArchivo.close()
    return  PalabrasBonus, ListaEstrofas


"""
***
Parametro 1 : Lista 
Parametro 2 : Lista
***
Tipo de Retorno: Lista
***
De cada estrofa de 4 versos (estrofa válida) se extrae la última palabra, además
se evalúa si alguna de estas últimas palabras coincide con alguna de las palabras 
bonus para luego poder sumarlo en el puntaje bruto.
Retorna una lista de tuplas, cada tulpa está compuesta por: una lista de las
últimas palabras de cada verso y el bonus.
"""
def ExtraerUltPalabra(PalabrasBonus, ListaEstrofas):
    UltPalVersos=[]
    
    for versos, validez in ListaEstrofas:
        if len(versos) == 4:
            UltimasPalabras= []
            for palabra in versos: 
                palabras = palabra.strip().split()
                ultima = palabras[-1].strip(',.´\'"')
                UltimasPalabras.append(ultima)

            Bonus = 0
            for ultima in UltimasPalabras:
                if ultima in PalabrasBonus:
                    Bonus = 2
                    break 
            
            UltPalVersos.append((UltimasPalabras, Bonus))

    return UltPalVersos

"""
***
Parametro 1 : String (str)
Parametro 2 : String (str)
***
Tipo de Retorno: Tupla
***
Se identifica el tipo de rima entre dos palabras. Primero se verifica la rima 
gemela, luego consonante y asonante y se verifica cual de estas dos da mayor puntaje 
para asignar la mejor rima, y finalmente se verifica la rima de misma terminación.
Retorna la mejor rima encontrada con su respectivo puntaje 
"""
def TipoRima(palabra1, palabra2):
    p1= CleanPalabra(palabra1)
    p2= CleanPalabra(palabra2)

    Rima=[]
    Puntaje=0
    #----GEMELA----
    if re.match(p1,p2):
        Puntaje=1
        return "Gemela", Puntaje
    
    #----CONSONANTE----
    # Rango desde la long completa de p1 hasta la 3ra letra, y empieza desde atras (-1)
    for i in range(len(p1), 2, -1):
        concidencia = re.match(PALABRA, p1[-i:])
        if concidencia:
            sufijo= concidencia.group()
            if re.search(sufijo, p2):
                CantidadLetras=len(sufijo)
                if CantidadLetras == 3 or CantidadLetras== 4:
                    Puntaje = 5
                else:
                    Puntaje=8
                Rima.append(("Consonante", Puntaje))
    
   #----ASONANTE----
    VocalesP1 = ""
    VocalesP2 = ""
    for vocal in re.findall(vocales, p1.lower()):
        VocalesP1 += vocal

    for vocal in re.findall(vocales, p2.lower()):
        VocalesP2 += vocal

    VocalesIguales= ""
    ContadorVocales= 0
    for i in range(1, min(len(VocalesP1), len(VocalesP2)) + 1):
        if VocalesP1[-i]==VocalesP2[-i]:
            VocalesIguales= VocalesP1[-i] + VocalesIguales
            ContadorVocales +=1

    if VocalesIguales and re.search(VocalesIguales, VocalesP2):
        if ContadorVocales == 1:
            Puntaje=3
        elif ContadorVocales == 2:
            Puntaje= 4
        elif ContadorVocales >= 3:
            Puntaje=8
        Rima.append(("Asonante", Puntaje))
    
    MejorRima= None
    for rima in Rima:
        if rima[0] in ("Consonante", "Asonante"):
            if MejorRima is None or rima[1]> MejorRima[1]:
                MejorRima = rima
    
    if MejorRima:
        return MejorRima
    
    #----MISMA TERMINACIÓN----
    for i in range(min(len(p1), len(p2)), 1, -1):
        coincidencia= p1[-i:]
        if re.search(coincidencia,p2):
            CantidadLetras= len(coincidencia)
            if CantidadLetras >= 2:
                Puntaje=2
            return "Misma Terminación", Puntaje
    
    return "No Riman", 0

"""
***
Parametro 1 : Lista
***
Tipo de Retorno: Lista
***
Se recorre una lista con las últimas palabras de cada verso y se evalúan todas las combinaciones
entre rimas utilizando la función TipoRima. Se calcula el puntaje bruto, con bonus o penalización 
en cada caso respectivo.
Retorna una lista compuesta por: una lista con las rimas identificadas, el puntaje final y el bonus
"""
def IDRimas(ListaUltPalabraVersos):
    
    Resultado= []

    for palabra, bonus in ListaUltPalabraVersos:
        v1, v2, v3, v4 = palabra

        pares = [(v1,v2),(v1,v3),(v1,v4),(v2,v3),(v2,v4),(v3,v4)]

        TotalPuntaje= 0
        RimaFinal= []
    

        for a,b in pares:
            Tipo, Puntos = TipoRima(a,b)
            if Tipo != "No Riman":
                TotalPuntaje += Puntos
                Descuento = False
            elif Tipo == "No Riman":
                Descuento = True 
            if Tipo not in RimaFinal:
                RimaFinal.append(Tipo)
        
        if Descuento:
            TotalPuntaje -=2

        TotalPuntaje= (TotalPuntaje + bonus )/5
        Resultado.append((RimaFinal, TotalPuntaje, bonus))
       
    return Resultado

"""
***
Parametro 1 : String (str)
***
Tipo de Retorno: None
***
Procesa un archivo usando las funciones IdentificarEstrofas(), ExtraerUltPalabra() e IDRimas().
Para luego generar un archivo de salida "decision.txt" donde se indica por estrofa el puntaje 
obtenido, si contiene la palabra bonus y los tipos de rima, en el caso de que la estrofa no
sea válida, esta se marca como "Inválida"
"""   
def ArchivoSalida(nombre_archivo):

    PalabrasBonus, ListaEstrofas = IdentificarEstrofas(nombre_archivo)
    ListaUltPalabraVersos = ExtraerUltPalabra(PalabrasBonus, ListaEstrofas)
    Resultado = IDRimas(ListaUltPalabraVersos)

    ArchSalida= "decision.txt"
    ArchSalida= open(ArchSalida,"w", encoding="utf-8")

    i=1
    indice_resultado=0
    for verso, validez in ListaEstrofas:
        RimaFinal, TotalPuntos, Bonus = Resultado[indice_resultado]

        if len(verso) != 4 or validez == False:
            ArchSalida.write("Estrofa {0}: Inválida\n".format(i))
        else:
            Ptos = f"{TotalPuntos:.1f}/10"
            if  Bonus > 0:
                ArchSalida.write("Estrofa {}: {} (BONUS)\n".format(i, Ptos))
            else:
                ArchSalida.write("Estrofa {}: {}\n".format(i, Ptos))
            if RimaFinal:
                ArchSalida.write("Rimas: " + ", ".join(RimaFinal) + "\n")
            indice_resultado +=1
        i+=1
    ArchSalida.close()

PalabrasBonus, ListaEstrofas = IdentificarEstrofas("estrofas.txt")
ListaUltPalabraVersos = ExtraerUltPalabra(PalabrasBonus, ListaEstrofas)
print(ArchivoSalida("estrofas.txt"))



