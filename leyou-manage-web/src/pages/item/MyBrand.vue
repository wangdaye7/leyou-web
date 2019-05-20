<template>
  <div>
    <v-layout class="px-4 py-1">
      <v-flex xs2>
        <v-btn color="info">新增品牌</v-btn>
      </v-flex>
      <v-spacer/>
      <v-flex xs6>
        <v-text-field label="search" hide-details append-icon="search"  v-model="key"></v-text-field>
      </v-flex>
    </v-layout>
    <v-data-table
      :headers="headers"
      :items="brands"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center"><img :src="props.item.image" /></td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="text-xs-center">
          <v-btn flat icon color="gray">
            <v-icon>edit</v-icon>
          </v-btn><v-btn flat icon color="error">
          <v-icon>delete</v-icon>
        </v-btn>
        </td>
      </template>
    </v-data-table>
  </div>
</template>

<script>
  export default {
    name: "MyBrand",
    data () {
      return {
        totalBrands: 0, // 总条数
        brands: [], // 当前页品牌数据
        loading: false, // 是否在加载中
        pagination: {}, // 分页信息
        key: "", //搜索条件
        headers: [ // 头信息
          {text: 'id', align: 'center', value: 'id'},
          {text: '名称', align: 'center', value: 'name', sortable: false},
          {text: 'LOGO', align: 'center', value: 'image', sortable: false},
          {text: '首字母', align: 'center', value: 'letter'},
          {text: '操作', align: 'center', },
        ]
      }
    },
    created(){
      /*this.brands = [
        {"id": 2032, "name": "OPPO", "image": "http://img10.360buyimg.com/popshop/jfs/t2119/133/2264148064/4303/b8ab3755/56b2f385N8e4eb051.jpg", "letter": "O",},
        {"id": 2033, "name": "飞利浦（PHILIPS）", "image":"http://img12.360buyimg.com/popshop/jfs/t18361/122/1318410299/1870/36fe70c9/5ac43a4dNa44a0ce0.jpg", "letter": "F",},
        {"id": 2034, "name": "华为（HUAWEI）", "image":"http://img10.360buyimg.com/popshop/jfs/t5662/36/8888655583/7806/1c629c01/598033b4Nd6055897.jpg", "letter": "H",},
        {"id": 2036, "name": "酷派（Coolpad）", "image":"http://img10.360buyimg.com/popshop/jfs/t2521/347/883897149/3732/91c917ec/5670cf96Ncffa2ae6.jpg", "letter": "K",},
        {"id": 2037, "name": "魅族（MEIZU）", "image": "http://img13.360buyimg.com/popshop/jfs/t3511/131/31887105/4943/48f83fa9/57fdf4b8N6e95624d.jpg", "letter": "M",
        },
      ],
      this.totalBrands = 15*/

      //向后台查询数据
      this.loadBrands();
    },
    methods:{
      loadBrands(){
        //查询开始, 加载进度条
        this.loading = true;
        //发起ajax请求
        this.$http.get("/item/brand/page", {
          params:{
            page: this.pagination.page, //当前页
            key: this.key, //搜索条件
            rows: this.pagination.rowsPerPage, //每页大小
            sortBy: this.pagination.sortBy,  //排序字段
            desc: this.pagination.descending,
          }
        }).then(resp =>{
          //处理异步查询后返回结果中的数据
          this.brands = resp.data.items;
          this.totalBrands = resp.data.total;
          //查询结束, 进度条关闭
          this.loading = false;
        })
      }
    },
    watch:{
      key(){
        //每次搜索之后 将当前页码跳转到1
        this.pagination.page = 1;
        this.loadBrands();
      },
      pagination:{
        deep:true,
        handler(){
          this.loadBrands();
        }
      }
    }
  }
</script>
