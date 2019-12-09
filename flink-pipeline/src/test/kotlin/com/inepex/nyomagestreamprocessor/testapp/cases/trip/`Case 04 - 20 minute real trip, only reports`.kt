package com.inepex.nyomagestreamprocessor.testapp.cases.trip

import com.inepex.nyomagestreamprocessor.api.incomingnyom.LocationOuterClass.Location
import com.inepex.nyomagestreamprocessor.api.incomingnyom.IncomingNyomEntryOuterClass
import com.inepex.nyomagestreamprocessor.schema.elastic.*
import com.inepex.nyomagestreamprocessor.schema.elastic.nyom.*
import com.inepex.nyomagestreamprocessor.schema.elastic.trip.Trip
import com.inepex.nyomagestreamprocessor.testapp.cases.latE5
import com.inepex.nyomagestreamprocessor.testapp.cases.lonE5
import com.inepex.nyomagestreamprocessor.testapp.common.incomingnyomstreambuilder.IncomingNyomStreamBuilder

class `Case 04 - 20 minute real trip, only reports` : TripCase() {
    override val entriesForFirstWindow =
            IncomingNyomStreamBuilder("1")
                    .apply {
                        // Tracker: 126041 (live) from: 1570427760000 to: 1570429800000
                        location(Location.newBuilder().setTimestamp(1570427785146).latE5(47.47678).lonE5(18.81186).build()) //0
                        location(Location.newBuilder().setTimestamp(1570427899604).latE5(47.4769).lonE5(18.81174).build()) //1
                        location(Location.newBuilder().setTimestamp(1570427905000).latE5(47.47692).lonE5(18.81175).build()) //2
                        location(Location.newBuilder().setTimestamp(1570427906000).latE5(47.47698).lonE5(18.81183).build()) //3
                        location(Location.newBuilder().setTimestamp(1570427918000).latE5(47.47736).lonE5(18.81274).build()) //4
                        location(Location.newBuilder().setTimestamp(1570427936000).latE5(47.47739).lonE5(18.81281).build()) //5
                        location(Location.newBuilder().setTimestamp(1570427937000).latE5(47.47741).lonE5(18.81283).build()) //6
                        location(Location.newBuilder().setTimestamp(1570427944000).latE5(47.47755).lonE5(18.81319).build()) //7
                        location(Location.newBuilder().setTimestamp(1570427974000).latE5(47.4785).lonE5(18.81518).build()) //8
                        location(Location.newBuilder().setTimestamp(1570427975000).latE5(47.47848).lonE5(18.81522).build()) //9
                        location(Location.newBuilder().setTimestamp(1570427976000).latE5(47.47846).lonE5(18.81524).build()) //10
                        location(Location.newBuilder().setTimestamp(1570427978000).latE5(47.47841).lonE5(18.81527).build()) //11
                        location(Location.newBuilder().setTimestamp(1570427994000).latE5(47.47766).lonE5(18.81598).build()) //12
                        location(Location.newBuilder().setTimestamp(1570428007000).latE5(47.47727).lonE5(18.81663).build()) //13
                        location(Location.newBuilder().setTimestamp(1570428008000).latE5(47.47727).lonE5(18.81667).build()) //14
                        location(Location.newBuilder().setTimestamp(1570428010000).latE5(47.4773).lonE5(18.81677).build()) //15
                        location(Location.newBuilder().setTimestamp(1570428016000).latE5(47.47745).lonE5(18.81716).build()) //16
                        location(Location.newBuilder().setTimestamp(1570428019000).latE5(47.47746).lonE5(18.81727).build()) //17
                        location(Location.newBuilder().setTimestamp(1570428020000).latE5(47.47745).lonE5(18.8173).build()) //18
                        location(Location.newBuilder().setTimestamp(1570428021000).latE5(47.47744).lonE5(18.81733).build()) //19
                        location(Location.newBuilder().setTimestamp(1570428052000).latE5(47.47523).lonE5(18.82038).build()) //20
                        location(Location.newBuilder().setTimestamp(1570428068000).latE5(47.47443).lonE5(18.82248).build()) //21
                        location(Location.newBuilder().setTimestamp(1570428069000).latE5(47.47444).lonE5(18.82256).build()) //22
                        location(Location.newBuilder().setTimestamp(1570428070000).latE5(47.47446).lonE5(18.82262).build()) //23
                        location(Location.newBuilder().setTimestamp(1570428071000).latE5(47.47449).lonE5(18.82266).build()) //24
                        location(Location.newBuilder().setTimestamp(1570428074000).latE5(47.47464).lonE5(18.82275).build()) //25
                        location(Location.newBuilder().setTimestamp(1570428080000).latE5(47.47501).lonE5(18.82287).build()) //26
                        location(Location.newBuilder().setTimestamp(1570428081000).latE5(47.47505).lonE5(18.82292).build()) //27
                        location(Location.newBuilder().setTimestamp(1570428114000).latE5(47.47633).lonE5(18.82632).build()) //28
                        location(Location.newBuilder().setTimestamp(1570428115000).latE5(47.47637).lonE5(18.82632).build()) //29
                        location(Location.newBuilder().setTimestamp(1570428116000).latE5(47.47641).lonE5(18.82632).build()) //30
                        location(Location.newBuilder().setTimestamp(1570428126000).latE5(47.477).lonE5(18.82615).build()) //31
                        location(Location.newBuilder().setTimestamp(1570428127000).latE5(47.47704).lonE5(18.82616).build()) //32
                        location(Location.newBuilder().setTimestamp(1570428128000).latE5(47.47706).lonE5(18.8262).build()) //33
                        location(Location.newBuilder().setTimestamp(1570428130000).latE5(47.47709).lonE5(18.82633).build()) //34
                        location(Location.newBuilder().setTimestamp(1570428146000).latE5(47.47743).lonE5(18.82822).build()) //35
                        location(Location.newBuilder().setTimestamp(1570428148000).latE5(47.47752).lonE5(18.82836).build()) //36
                        location(Location.newBuilder().setTimestamp(1570428201000).latE5(47.47809).lonE5(18.82929).build()) //37
                        location(Location.newBuilder().setTimestamp(1570428233000).latE5(47.4801).lonE5(18.83337).build()) //38
                        location(Location.newBuilder().setTimestamp(1570428235000).latE5(47.48011).lonE5(18.83355).build()) //39
                        location(Location.newBuilder().setTimestamp(1570428236000).latE5(47.48009).lonE5(18.83364).build()) //40
                        location(Location.newBuilder().setTimestamp(1570428239000).latE5(47.47997).lonE5(18.83381).build()) //41
                        location(Location.newBuilder().setTimestamp(1570428249000).latE5(47.47968).lonE5(18.83421).build()) //42
                        location(Location.newBuilder().setTimestamp(1570428295000).latE5(47.47979).lonE5(18.83418).build()) //43
                        location(Location.newBuilder().setTimestamp(1570428303000).latE5(47.48002).lonE5(18.83383).build()) //44
                        location(Location.newBuilder().setTimestamp(1570428310000).latE5(47.48016).lonE5(18.83373).build()) //45
                        location(Location.newBuilder().setTimestamp(1570428313000).latE5(47.48025).lonE5(18.83367).build()) //46
                        location(Location.newBuilder().setTimestamp(1570428314000).latE5(47.48027).lonE5(18.83363).build()) //47
                        location(Location.newBuilder().setTimestamp(1570428315000).latE5(47.48027).lonE5(18.83357).build()) //48
                        location(Location.newBuilder().setTimestamp(1570428316000).latE5(47.48025).lonE5(18.83353).build()) //49
                        location(Location.newBuilder().setTimestamp(1570428328000).latE5(47.48009).lonE5(18.83314).build()) //50
                        location(Location.newBuilder().setTimestamp(1570428329000).latE5(47.4801).lonE5(18.8331).build()) //51
                        location(Location.newBuilder().setTimestamp(1570428331000).latE5(47.48011).lonE5(18.83303).build()) //52
                        location(Location.newBuilder().setTimestamp(1570428332000).latE5(47.4801).lonE5(18.833).build()) //53
                        location(Location.newBuilder().setTimestamp(1570428344000).latE5(47.47985).lonE5(18.83238).build()) //54
                        location(Location.newBuilder().setTimestamp(1570428345000).latE5(47.47983).lonE5(18.83235).build()) //55
                        location(Location.newBuilder().setTimestamp(1570428346000).latE5(47.47979).lonE5(18.83233).build()) //56
                        location(Location.newBuilder().setTimestamp(1570428347000).latE5(47.47975).lonE5(18.83233).build()) //57
                        location(Location.newBuilder().setTimestamp(1570428348000).latE5(47.47973).lonE5(18.83235).build()) //58
                        location(Location.newBuilder().setTimestamp(1570428358000).latE5(47.47969).lonE5(18.83246).build()) //59
                        location(Location.newBuilder().setTimestamp(1570428359000).latE5(47.47969).lonE5(18.83253).build()) //60
                        location(Location.newBuilder().setTimestamp(1570428360000).latE5(47.47972).lonE5(18.8326).build()) //61
                        location(Location.newBuilder().setTimestamp(1570428377000).latE5(47.48013).lonE5(18.83346).build()) //62
                        location(Location.newBuilder().setTimestamp(1570428379000).latE5(47.48012).lonE5(18.83362).build()) //63
                        location(Location.newBuilder().setTimestamp(1570428381000).latE5(47.48004).lonE5(18.83377).build()) //64
                        location(Location.newBuilder().setTimestamp(1570428396000).latE5(47.47922).lonE5(18.83494).build()) //65
                        location(Location.newBuilder().setTimestamp(1570428397000).latE5(47.47917).lonE5(18.83497).build()) //66
                        location(Location.newBuilder().setTimestamp(1570428399000).latE5(47.47902).lonE5(18.83495).build()) //67
                        location(Location.newBuilder().setTimestamp(1570428401000).latE5(47.47891).lonE5(18.83487).build()) //68
                        location(Location.newBuilder().setTimestamp(1570428402000).latE5(47.47888).lonE5(18.8348).build()) //69
                        location(Location.newBuilder().setTimestamp(1570428403000).latE5(47.47888).lonE5(18.83472).build()) //70
                        location(Location.newBuilder().setTimestamp(1570428404000).latE5(47.47891).lonE5(18.83465).build()) //71
                        location(Location.newBuilder().setTimestamp(1570428410000).latE5(47.4791).lonE5(18.83436).build()) //72
                        location(Location.newBuilder().setTimestamp(1570428411000).latE5(47.47908).lonE5(18.83429).build()) //73
                        location(Location.newBuilder().setTimestamp(1570428415000).latE5(47.4789).lonE5(18.83398).build()) //74
                        location(Location.newBuilder().setTimestamp(1570428430000).latE5(47.4783).lonE5(18.83297).build()) //75
                        location(Location.newBuilder().setTimestamp(1570428431000).latE5(47.47826).lonE5(18.83298).build()) //76
                        location(Location.newBuilder().setTimestamp(1570428433000).latE5(47.47818).lonE5(18.83306).build()) //77
                        location(Location.newBuilder().setTimestamp(1570428449000).latE5(47.4775).lonE5(18.83384).build()) //78
                        location(Location.newBuilder().setTimestamp(1570428453000).latE5(47.47734).lonE5(18.83393).build()) //79
                        location(Location.newBuilder().setTimestamp(1570428485000).latE5(47.47526).lonE5(18.83642).build()) //80
                        location(Location.newBuilder().setTimestamp(1570428486000).latE5(47.47521).lonE5(18.83641).build()) //81
                        location(Location.newBuilder().setTimestamp(1570428487000).latE5(47.47517).lonE5(18.83639).build()) //82
                        location(Location.newBuilder().setTimestamp(1570428488000).latE5(47.47513).lonE5(18.83633).build()) //83
                        location(Location.newBuilder().setTimestamp(1570428500000).latE5(47.47466).lonE5(18.83548).build()) //84
                        location(Location.newBuilder().setTimestamp(1570428501000).latE5(47.47462).lonE5(18.83548).build()) //85
                        location(Location.newBuilder().setTimestamp(1570428502000).latE5(47.47456).lonE5(18.83551).build()) //86
                        location(Location.newBuilder().setTimestamp(1570428532000).latE5(47.4741).lonE5(18.83607).build()) //87
                        location(Location.newBuilder().setTimestamp(1570428533000).latE5(47.47409).lonE5(18.83611).build()) //88
                        location(Location.newBuilder().setTimestamp(1570428534000).latE5(47.4741).lonE5(18.83618).build()) //89
                        location(Location.newBuilder().setTimestamp(1570428535000).latE5(47.47412).lonE5(18.83624).build()) //90
                        location(Location.newBuilder().setTimestamp(1570428541000).latE5(47.47434).lonE5(18.83672).build()) //91
                        location(Location.newBuilder().setTimestamp(1570428543000).latE5(47.47434).lonE5(18.83691).build()) //92
                        location(Location.newBuilder().setTimestamp(1570428545000).latE5(47.47427).lonE5(18.83708).build()) //93
                        location(Location.newBuilder().setTimestamp(1570428546000).latE5(47.47422).lonE5(18.83715).build()) //94
                        location(Location.newBuilder().setTimestamp(1570428548000).latE5(47.47406).lonE5(18.83724).build()) //95
                        location(Location.newBuilder().setTimestamp(1570428556000).latE5(47.47352).lonE5(18.8374).build()) //96
                        location(Location.newBuilder().setTimestamp(1570428557000).latE5(47.4735).lonE5(18.83747).build()) //97
                        location(Location.newBuilder().setTimestamp(1570428559000).latE5(47.47352).lonE5(18.8376).build()) //98
                        location(Location.newBuilder().setTimestamp(1570428560000).latE5(47.47354).lonE5(18.83765).build()) //99
                        location(Location.newBuilder().setTimestamp(1570428561000).latE5(47.47358).lonE5(18.83768).build()) //100
                        location(Location.newBuilder().setTimestamp(1570428562000).latE5(47.47362).lonE5(18.83768).build()) //101
                        location(Location.newBuilder().setTimestamp(1570428563000).latE5(47.47367).lonE5(18.83767).build()) //102
                        location(Location.newBuilder().setTimestamp(1570428567000).latE5(47.47385).lonE5(18.83762).build()) //103
                        // wait
                        location(Location.newBuilder().setTimestamp(1570428840075).latE5(47.47403).lonE5(18.838).build()) //104
                        location(Location.newBuilder().setTimestamp(1570429111000).latE5(47.47393).lonE5(18.83773).build()) //105
                        location(Location.newBuilder().setTimestamp(1570429242000).latE5(47.47386).lonE5(18.83757).build()) //106
                        location(Location.newBuilder().setTimestamp(1570429246000).latE5(47.47364).lonE5(18.83755).build()) //107
                        location(Location.newBuilder().setTimestamp(1570429247000).latE5(47.4736).lonE5(18.83757).build()) //108
                        location(Location.newBuilder().setTimestamp(1570429248000).latE5(47.47356).lonE5(18.83756).build()) //109
                        location(Location.newBuilder().setTimestamp(1570429249000).latE5(47.47353).lonE5(18.83755).build()) //110
                        location(Location.newBuilder().setTimestamp(1570429250000).latE5(47.47351).lonE5(18.83753).build()) //111
                        location(Location.newBuilder().setTimestamp(1570429251000).latE5(47.4735).lonE5(18.83749).build()) //112
                        location(Location.newBuilder().setTimestamp(1570429252000).latE5(47.47351).lonE5(18.83745).build()) //113
                        location(Location.newBuilder().setTimestamp(1570429253000).latE5(47.47352).lonE5(18.83741).build()) //114
                        location(Location.newBuilder().setTimestamp(1570429254000).latE5(47.47356).lonE5(18.83739).build()) //115
                        location(Location.newBuilder().setTimestamp(1570429255000).latE5(47.47361).lonE5(18.83739).build()) //116
                        location(Location.newBuilder().setTimestamp(1570429257000).latE5(47.47372).lonE5(18.83738).build()) //117
                        location(Location.newBuilder().setTimestamp(1570429268000).latE5(47.47425).lonE5(18.8372).build()) //118
                        location(Location.newBuilder().setTimestamp(1570429269000).latE5(47.47431).lonE5(18.83722).build()) //119
                        location(Location.newBuilder().setTimestamp(1570429270000).latE5(47.47435).lonE5(18.83722).build()) //120
                        location(Location.newBuilder().setTimestamp(1570429271000).latE5(47.47439).lonE5(18.83719).build()) //121
                        location(Location.newBuilder().setTimestamp(1570429272000).latE5(47.47442).lonE5(18.83713).build()) //122
                        location(Location.newBuilder().setTimestamp(1570429273000).latE5(47.47443).lonE5(18.83707).build()) //123
                        location(Location.newBuilder().setTimestamp(1570429280000).latE5(47.47429).lonE5(18.83654).build()) //124
                        location(Location.newBuilder().setTimestamp(1570429284000).latE5(47.47417).lonE5(18.8362).build()) //125
                        location(Location.newBuilder().setTimestamp(1570429285000).latE5(47.47416).lonE5(18.83613).build()) //126
                        location(Location.newBuilder().setTimestamp(1570429286000).latE5(47.47417).lonE5(18.83604).build()) //127
                        location(Location.newBuilder().setTimestamp(1570429287000).latE5(47.47419).lonE5(18.83598).build()) //128
                        location(Location.newBuilder().setTimestamp(1570429300000).latE5(47.47434).lonE5(18.83581).build()) //129
                        location(Location.newBuilder().setTimestamp(1570429302000).latE5(47.47436).lonE5(18.83576).build()) //130
                        location(Location.newBuilder().setTimestamp(1570429394543).latE5(47.47427).lonE5(18.83584).build()) //131
                        location(Location.newBuilder().setTimestamp(1570429548000).latE5(47.47447).lonE5(18.83549).build()) //132
                        location(Location.newBuilder().setTimestamp(1570429549000).latE5(47.47457).lonE5(18.83537).build()) //133
                        location(Location.newBuilder().setTimestamp(1570429556000).latE5(47.47505).lonE5(18.83492).build()) //134
                        location(Location.newBuilder().setTimestamp(1570429562000).latE5(47.47548).lonE5(18.83445).build()) //135
                        location(Location.newBuilder().setTimestamp(1570429610000).latE5(47.4777).lonE5(18.8317).build()) //136
                        location(Location.newBuilder().setTimestamp(1570429617000).latE5(47.47749).lonE5(18.83138).build()) //137
                        location(Location.newBuilder().setTimestamp(1570429626000).latE5(47.47728).lonE5(18.83122).build()) //138
                        location(Location.newBuilder().setTimestamp(1570429629000).latE5(47.4772).lonE5(18.8312).build()) //139
                        location(Location.newBuilder().setTimestamp(1570429637000).latE5(47.47705).lonE5(18.83119).build()) //140
                        location(Location.newBuilder().setTimestamp(1570429644000).latE5(47.47697).lonE5(18.83104).build()) //141
                        location(Location.newBuilder().setTimestamp(1570429658000).latE5(47.47746).lonE5(18.83001).build()) //142
                        location(Location.newBuilder().setTimestamp(1570429679000).latE5(47.47822).lonE5(18.82927).build()) //143
                        location(Location.newBuilder().setTimestamp(1570429694000).latE5(47.47782).lonE5(18.82857).build()) //144
                        location(Location.newBuilder().setTimestamp(1570429700000).latE5(47.47741).lonE5(18.82806).build()) //145
                        location(Location.newBuilder().setTimestamp(1570429719000).latE5(47.47706).lonE5(18.82614).build()) //146
                        location(Location.newBuilder().setTimestamp(1570429720000).latE5(47.47704).lonE5(18.82613).build()) //147
                        location(Location.newBuilder().setTimestamp(1570429723000).latE5(47.47694).lonE5(18.82612).build()) //148
                        location(Location.newBuilder().setTimestamp(1570429733000).latE5(47.47636).lonE5(18.82621).build()) //149
                        location(Location.newBuilder().setTimestamp(1570429734000).latE5(47.47633).lonE5(18.82621).build()) //150
                        location(Location.newBuilder().setTimestamp(1570429735000).latE5(47.4763).lonE5(18.82618).build()) //151
                        location(Location.newBuilder().setTimestamp(1570429742000).latE5(47.47616).lonE5(18.82578).build()) //152
                        location(Location.newBuilder().setTimestamp(1570429772000).latE5(47.47494).lonE5(18.82255).build()) //153
                        location(Location.newBuilder().setTimestamp(1570429773000).latE5(47.47496).lonE5(18.82253).build()) //154
                        location(Location.newBuilder().setTimestamp(1570429775000).latE5(47.47501).lonE5(18.82251).build()) //155
                    }.getStream()

    override val expectedNumberOfTripInserts = 2

    override val expectedTrips = listOf(
            Trip(1L, GeoPoint(47.47692, 18.81175), GeoPoint(47.47386, 18.83757),
                    1570427905000L, 1570428567000L, 675000,
                    "LINESTRING (18.81175 47.47692, 18.81183 47.47698, 18.81274 47.47736, 18.81281 47.47739, 18.81283 47.47741, 18.81318 47.47755, 18.81518 47.4785, 18.81522 47.47848, 18.81524 47.47846, 18.81527 47.47841, 18.81598 47.47766, 18.81663 47.47727, 18.81666 47.47727, 18.81677 47.4773, 18.81716 47.47745, 18.81727 47.47746, 18.8173 47.47745, 18.81732 47.47744, 18.82038 47.47523, 18.82247 47.47443, 18.82256 47.47444, 18.82262 47.47446, 18.82266 47.47449, 18.82275 47.47464, 18.82287 47.47501, 18.82292 47.47505, 18.82632 47.47633, 18.82632 47.47637, 18.82632 47.47641, 18.82614 47.477, 18.82616 47.47704, 18.8262 47.47706, 18.82632 47.47709, 18.82822 47.47743, 18.82836 47.47752, 18.82929 47.47809, 18.83336 47.4801, 18.83355 47.48011, 18.83364 47.48009, 18.83381 47.47997, 18.8342 47.47968, 18.83418 47.47979, 18.83383 47.48002, 18.83373 47.48016, 18.83367 47.48025, 18.83363 47.48027, 18.83357 47.48027, 18.83353 47.48025, 18.83314 47.48009, 18.8331 47.4801, 18.83303 47.48011, 18.83299 47.4801, 18.83238 47.47985, 18.83235 47.47983, 18.83233 47.47979, 18.83233 47.47975, 18.83235 47.47973, 18.83246 47.47969, 18.83252 47.47969, 18.8326 47.47972, 18.83345 47.48013, 18.83362 47.48012, 18.83377 47.48004, 18.83494 47.47922, 18.83496 47.47917, 18.83495 47.47902, 18.83486 47.47891, 18.8348 47.47888, 18.83472 47.47888, 18.83465 47.47891, 18.83436 47.4791, 18.83429 47.47908, 18.83398 47.4789, 18.83297 47.4783, 18.83298 47.47826, 18.83306 47.47818, 18.83383 47.4775, 18.83392 47.47734, 18.83642 47.47526, 18.83641 47.47521, 18.83639 47.47517, 18.83633 47.47513, 18.83548 47.47466, 18.83548 47.47462, 18.83551 47.47456, 18.83607 47.4741, 18.83611 47.47409, 18.83617 47.4741, 18.83624 47.47412, 18.83672 47.47434, 18.83691 47.47434, 18.83708 47.47427, 18.83715 47.47422, 18.83724 47.47406, 18.83739 47.47352, 18.83747 47.4735, 18.83759 47.47352, 18.83765 47.47354, 18.83768 47.47358, 18.83768 47.47362, 18.83767 47.47367, 18.83762 47.47385, 18.83757 47.47386)",
                    emptyList(), Mapping("object1", "", "")),
            Trip(1L, GeoPoint(47.47386, 18.83757), GeoPoint(47.47447, 18.83549),
                    1570429242000L, 1570429394543L, 153457,
                    "LINESTRING (18.83757 47.47386, 18.83755 47.47364, 18.83757 47.4736, 18.83756 47.47356, 18.83755 47.47353, 18.83753 47.47351, 18.83749 47.4735, 18.83745 47.47351, 18.8374 47.47352, 18.83739 47.47356, 18.83739 47.47361, 18.83738 47.47372, 18.8372 47.47425, 18.83721 47.47431, 18.83721 47.47435, 18.83719 47.47439, 18.83712 47.47442, 18.83707 47.47443, 18.83654 47.47429, 18.8362 47.47417, 18.83613 47.47416, 18.83604 47.47417, 18.83598 47.47419, 18.8358 47.47434, 18.83576 47.47436, 18.83584 47.47427, 18.83549 47.47447)",
                    emptyList(), Mapping("object1", "", ""))
    )
}
