package com.zhuhy.esapi;

import com.alibaba.fastjson.JSON;
import com.zhuhy.esapi.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EsApiApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;


    /**
     * 测试索引的创建
     */
    @Test
    void testCreateIndex() throws IOException {
        // 1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("demo_index");
        // 2.客户端执行请求 IndicesClient,请求后获得响应
        CreateIndexResponse createIndexResponse =
                client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    /**
     * 测试获取索引
     */
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("demo_index");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 测试删除索引
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("demo_index");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * 测试添加文档
     */
    @Test
    void testADdDocument() throws IOException {
        // 创建对象
        User user = new User("祝洪洋", 3);
        // 创建请求
        IndexRequest request = new IndexRequest("demo_index");

        // 规则 PUT /demo_index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));

        // 把数据放入请求
        request.source(JSON.toJSONString(user), XContentType.JSON);

        // 客户端发送请求,获得响应的结果
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());


    }

    /**
     * 测试获取文档
     */
    @Test
    void testIsExiste() throws IOException {
        // 判断文档是否存在
        GetRequest request = new GetRequest("demo_index", "1");
        // 不获取返回的_source上下文
//        request.fetchSourceContext(new FetchSourceContext(false));
//        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);

        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString()); // 打印文档的内容
        System.out.println(getResponse);
    }

    /**
     * 更新文档的信息
     */
    @Test
    void testUpdateRequest() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("demo_index", "1");
        updateRequest.timeout("1s");
        User user = new User("狂神说Java", 18);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 删除文档记录
     */
    @Test
    void testDeleteRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("demo_index", "1");
        request.timeout("1s");
        DeleteResponse deleteResponse = client.delete(request,
                RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

}
