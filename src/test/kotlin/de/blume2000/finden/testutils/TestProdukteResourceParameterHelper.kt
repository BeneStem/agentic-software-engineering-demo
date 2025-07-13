package de.blume2000.finden.testutils

internal fun erstelleValideProdukteResourceParameter(): String = """
      {
        ${erstelleValideCmsProdukteFilter()},
        ${erstelleValideUserProdukteFilter()}
      }
    """.trimIndent()

internal fun erstelleValideCmsProdukteFilter(): String = """
      "cmsProdukteFilter": {
        ${erstelleValideKlassifikationen()},
        ${erstelleValidenPreisbereich()},
        ${erstelleValideProduktnummern()},
        ${erstelleValideProduktnummerVerwendung()},
        ${erstelleValidesLimit()},
        ${erstelleValideFarben()},
        ${erstelleValideBlumensorten()}
      }
    """.trimIndent()

internal fun erstelleValideUserProdukteFilter(): String = """
      "userProdukteFilter": {
        ${erstelleValideSorten()},
        ${erstelleValideFarben()},
        ${erstelleValidenPreisbereich()},
        ${erstelleValideKlassifikationen()},
        ${erstelleValideLiefertage()}
      }
    """.trimIndent()

internal fun erstelleValideKlassifikation(zahl: Int = 1): String = """
      {
        "name": "Klassifikation$zahl",
        "id": "$zahl"
      }
    """.trimIndent()

internal fun erstelleValideKlassifikationen(): String = """
      "klassifikationen": [
        ${erstelleValideKlassifikation(1)},
        ${erstelleValideKlassifikation(2)}
      ]
    """.trimIndent()

internal fun erstelleLeereKlassifikationen(): String = """
      "klassifikationen": []
    """.trimIndent()

internal fun erstelleValidenMinPreis(preis: Number = 1): String = """
      "minPreis": {
        "bruttoBetrag": $preis,
        "waehrung": "EUR"
      }
    """.trimIndent()

internal fun erstelleValidenMaxPreis(preis: Number = 100): String = """
      "maxPreis": {
        "bruttoBetrag": $preis,
        "waehrung":"EUR"
      }
    """.trimIndent()

internal fun erstelleValidenPreisbereich(): String = """
      "preisbereich": {
        ${erstelleValidenMinPreis()},
        ${erstelleValidenMaxPreis()}
      }
    """.trimIndent()

internal fun erstelleValideProduktnummer(nummer: String = "111"): String = """
      {
        "nummer": "$nummer"
      }
    """.trimIndent()

internal fun erstelleValideProduktnummern(): String = """
      "produktNummern": [
        ${erstelleValideProduktnummer("111")},
        ${erstelleValideProduktnummer("123")}
      ]
    """.trimIndent()

internal fun erstelleValideProduktnummerVerwendung(): String = """
      "produktnummernVerwendung": "selektionsbasis"
    """.trimIndent()

internal fun erstelleValidesLimit(): String = """
      "limit": 10
    """.trimIndent()

internal fun erstelleValideFarbe(farbe: String = "grün"): String = """
      {
        "name": "$farbe"
      }
    """.trimIndent()

internal fun erstelleValideFarben(): String = """
      "farben": [
        ${erstelleValideFarbe("grün")},
        ${erstelleValideFarbe("gelb")}
      ]
    """.trimIndent()

internal fun erstelleLeereFarben(): String = """
      "farben": []
    """.trimIndent()

internal fun erstelleValideBlumensorte(sorte: String = "Rosen"): String = """
      {
        "name": "$sorte"
      }
    """.trimIndent()

internal fun erstelleValideBlumensorten(): String = """
      "blumensorten": [
        ${erstelleValideBlumensorte("Rose")},
        ${erstelleValideBlumensorte("Lilie")}
      ]
    """.trimIndent()

internal fun erstelleValideSorte(sorte: String = "Rosen"): String = """
      {
        "name": "$sorte"
      }
    """.trimIndent()

internal fun erstelleValideSorten(): String = """
      "sorten": [
        ${erstelleValideSorte("Rose")},
        ${erstelleValideSorte("Lilie")}
      ]
    """.trimIndent()

internal fun erstelleLeereSorten(): String = """
      "sorten": []
    """.trimIndent()

internal fun erstelleValideLiefertage(): String = """
      "liefertage": {
        "verfuegbareLiefertage": [
          "2022-01-01",
          "2022-01-12"
        ]
      }
    """.trimIndent()

internal fun erstelleLeereLiefertage(): String = """
      "liefertage": {
        "verfuegbareLiefertage": []
      }
    """.trimIndent()

@Suppress("LongParameterList")
internal fun erstelleProdukteResourceParameter(
  cmsKlassifikationen: String? = null,
  cmsPreisbereich: String? = null,
  cmsProduktnummern: String? = null,
  cmsProduktnummernVerwendung: String? = null,
  cmsLimit: String? = null,
  cmsFarben: String? = null,
  cmsBlumensorten: String? = null,
  userSorten: String? = null,
  userFarben: String? = null,
  userPreisbereich: String? = null,
  userKlassifikationen: String? = null,
  userLiefertage: String? = null
): String = """
      {
        "cmsProdukteFilter": {
          <cmsKlassifikationen>,
          <cmsPreisbereich>,
          <cmsProduktnummern>,
          <cmsProduktnummernVerwendung>,
          <cmsLimit>,
          <cmsFarben>,
          <cmsBlumensorten>
        },
        "userProdukteFilter": {
          <userSorten>,
          <userFarben>,
          <userPreisbereich>,
          <userKlassifikationen>,
          <userLiefertage>
        }
      }
    """
  .replace("<cmsKlassifikationen>", cmsKlassifikationen ?: erstelleValideKlassifikationen())
  .replace("<cmsPreisbereich>", cmsPreisbereich ?: erstelleValidenPreisbereich())
  .replace("<cmsProduktnummern>", cmsProduktnummern ?: erstelleValideProduktnummern())
  .replace("<cmsProduktnummernVerwendung>", cmsProduktnummernVerwendung ?: erstelleValideProduktnummerVerwendung())
  .replace("<cmsLimit>", cmsLimit ?: erstelleValidesLimit())
  .replace("<cmsFarben>", cmsFarben ?: erstelleValideFarben())
  .replace("<cmsBlumensorten>", cmsBlumensorten ?: erstelleValideBlumensorten())
  .replace("<userSorten>", userSorten ?: erstelleValideSorten())
  .replace("<userFarben>", userFarben ?: erstelleValideFarben())
  .replace("<userPreisbereich>", userPreisbereich ?: erstelleValidenPreisbereich())
  .replace("<userKlassifikationen>", userKlassifikationen ?: erstelleValideKlassifikationen())
  .replace("<userLiefertage>", userLiefertage ?: erstelleValideLiefertage())
  .trimIndent()
