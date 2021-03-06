package com.yahoo.maha.core.fact

import scala.collection.SortedSet

class FkFactMapStorageTest extends BaseFactTest {

  test("test default powerset storage") {
    val result = publicFact(fact1, fkFactMapStorage = new PowerSetFkFactMapStorage).getFkFactMapStorage
    assert(result.size == 15)
    val searchResult = result.search(SortedSet("account_id", "campaign_id"))
    assert(searchResult.isDefined)
    assert(searchResult.get.head.name === "fact1")
    assert(result.search(SortedSet("ad_id", "blah_id")).isEmpty)
    assert(result.search(SortedSet("account_id", "ad_id", "campaign_id", "ad_group_id")).get.head.name === "fact1")
  }

  test("test rocksb powerset storage") {
    val result = publicFact(fact1, fkFactMapStorage = RocksDBFkFactMapStorage(Some("target/test"))).getFkFactMapStorage
    assert(result.size == 15)
    val searchResult = result.search(SortedSet("account_id", "campaign_id"))
    assert(searchResult.isDefined)
    assert(searchResult.get.head.name === "fact1")
    assert(result.search(SortedSet("ad_id", "blah_id")).isEmpty)
    assert(result.search(SortedSet("account_id", "ad_id", "campaign_id", "ad_group_id")).get.head.name === "fact1")
  }

  test("test roaring bitmap powerset storage") {
    val result = publicFact(fact1, fkFactMapStorage = RoaringBitmapFkFactMapStorage()).getFkFactMapStorage
    assert(result.size === 1)
    val searchResult = result.search(SortedSet("account_id", "campaign_id"))
    assert(searchResult.isDefined)
    assert(searchResult.get.head.name === "fact1")

    assert(result.search(SortedSet("ad_id", "blah_id")).isEmpty)
    assert(result.search(SortedSet("account_id", "ad_id", "campaign_id", "ad_group_id")).get.head.name === "fact1")
  }
}
