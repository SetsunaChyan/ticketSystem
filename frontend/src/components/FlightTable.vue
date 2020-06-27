<template>
    <div>
        <el-form :inline="true" ref="form" :model="form" label-width="50px">
            <el-form-item label="出发">
                <el-input v-model="form.depart" placeholder="输入出发地"></el-input>
            </el-form-item>
            <el-form-item label="到达">
                <el-input v-model="form.dest" placeholder="输入目的地"></el-input>
            </el-form-item>
            <el-form-item label="日期">
                <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" v-model="form.date"
                                style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="flightID" label="航班编号" width="180">
            </el-table-column>
            <el-table-column prop="flightCompany" label="航空公司" width="180">
            </el-table-column>
            <el-table-column prop="departure" label="出发地" width="180">
            </el-table-column>
            <el-table-column prop="departureTime" label="出发时间" width="180">
            </el-table-column>
            <el-table-column prop="arrival" label="到达地" width="180">
            </el-table-column>
            <el-table-column prop="arrivalTime" label="到达时间" width="180">
            </el-table-column>
            <el-table-column prop="price" label="价格" width="180">
            </el-table-column>
            <el-table-column prop="rest" label="余量" width="180">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleBuy(scope.$index, scope.row)">购买</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: 'FlightTable',
        data: function () {
            return {
                form: {
                    depart: '',
                    dest: '',
                    date: ''
                },
                tableData: []
            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            handleBuy(index, row) {
                console.log(row.flightID);
                const that = this;
                axios.get('api/buyFlight', {
                    params: row
                }).then(function (ret) {
                    console.log(ret)
                    if (ret.data.data.key) {
                        that.onSearch();
                    }
                    alert(ret.data.data.value);
                }, function () {
                    console.log('传输失败');
                });
            },
            onSearch() {
                const that = this;
                if(this.form.date==null) this.form.date='';
                axios.get('api/getFlight', {
                    params: this.form
                }).then(function (ret) {
                    console.log(ret)
                    if (ret.data.status === 0) {
                        that.tableData = ret.data.data;
                    }
                }, function () {
                    console.log('传输失败');
                });
            }
        }
    }
</script>
